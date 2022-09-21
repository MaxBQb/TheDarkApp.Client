package lab.maxb.dark.presentation.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.presentation.extra.*
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.viewModel.utils.UiState
import lab.maxb.dark.presentation.viewModel.utils.stateIn
import lab.maxb.dark.presentation.viewModel.utils.valueOrNull
import org.koin.android.annotation.KoinViewModel


data class AuthUiState(
    val login: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val passwordRepeat: String = "",
    val isAccountNew: Boolean = false,
    val isLoading: Boolean = false,
)

sealed interface AuthUiEvent {
    data class LoginChanged(val login: String): AuthUiEvent
    data class PasswordChanged(val password: String): AuthUiEvent
    data class PasswordRepeatChanged(val password: String): AuthUiEvent
    data class PasswordVisibilityChanged(val showPassword: Boolean): AuthUiEvent
    data class RegistrationNeededChanged(val isAccountNew: Boolean): AuthUiEvent
    object Submit: AuthUiEvent
}


@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class AuthViewModel(
    private val profileRepository: ProfileRepository,
    private val usersRepository: UsersRepository,
    private val db: LocalDatabase,
    private val userSettings: UserSettings,
) : ViewModel() {
    private var _wasAuthorized = false
    private val _profile = MutableStateFlow(UiState.Loading as UiState<Profile?>)
    val profile = _profile.stateIn(UiState.Loading)

    val user = profile.filter {
        it.valueOrNull != null
    }.flatMapLatest {
        usersRepository.getUser(it.valueOrNull!!.user!!.id)
    }.stateIn(null)

    private val errorChannel = Channel<UiText>()
    val errors = errorChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AuthUiEvent) = with(event) {
        when (this) {
            is AuthUiEvent.LoginChanged -> _uiState.update {
                it.copy(login = login)
            }
            is AuthUiEvent.PasswordChanged -> _uiState.update {
                it.copy(password = password)
            }
            is AuthUiEvent.PasswordRepeatChanged -> _uiState.update {
                it.copy(passwordRepeat = password)
            }
            is AuthUiEvent.PasswordVisibilityChanged -> _uiState.update {
                it.copy(showPassword = showPassword)
            }
            is AuthUiEvent.RegistrationNeededChanged -> _uiState.update {
                it.copy(isAccountNew = isAccountNew)
            }
            is AuthUiEvent.Submit -> authorize()
        }
    }


    private fun setLoading(isLoading: Boolean) = _uiState.update {
        it.copy(isLoading = isLoading)
    }

    private fun setError(error: UiText) { launch {
        errorChannel.send(error)
        setLoading(false)
    } }

    private fun reset() = _uiState.update {
        AuthUiState(
            login = it.login,
        )
    }

    init {
        launch {
            profileRepository.profile.mapLatest {
                UiState.Success(it)
            }.catch {
                UiState.Error<Profile?>(it)
            }.also {
                _profile.emitAll(it)
            }
        }
    }

    fun handleNotAuthorizedYet() {
        _profile.value = UiState.Loading
    }

    fun handleAuthorizedStateChanges() = _profile.value.ifLoaded {
        if (it != null)
            _wasAuthorized = true
        else if (_wasAuthorized) {
            _wasAuthorized = false
            signOut()
        }
    }

    private fun getFieldsErrors() = when {
        hasEmptyFields() -> uiTextOf(R.string.auth_message_hasEmptyFields)
        isPasswordsNotMatch() -> uiTextOf(R.string.auth_message_passwordsNotMatch)
        else -> UiText.Empty
    }

    private fun authorize() {
        val state = uiState.value
        if (state.isLoading)
            return
        val error = getFieldsErrors()
        if (error.isNotEmpty)
            return setError(error)
        setLoading(true)
        _profile.value = UiState.Loading
        profileRepository.sendCredentials(
            AuthCredentials(
                state.login,
                state.password,
                state.isAccountNew,
            )
        )
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    fun authorizeByOAUTHProvider(login: String, name: String, authCode: String) {
        TODO()
    }

    fun hasEmptyFields() = with(uiState.value) {
        login.isEmpty() ||
                password.isEmpty() ||
                isAccountNew && passwordRepeat.isEmpty()
    }

    fun isPasswordsNotMatch() = with(uiState.value) {
        isAccountNew && password != passwordRepeat
    }

    fun signOut() = launch(Dispatchers.Default) {
        userSettings.token = ""
        userSettings.login = ""
        setLoading(false)
        db.clearAllTables()
    }

    fun handleAuthResult(profile: Profile?, onAuthorized: () -> Unit) {
        val state = uiState.value
        if (!state.isLoading)
            return
        profile?.let {
            reset()
            onAuthorized()
            return
        }
        val message = if (state.isAccountNew)
            R.string.auth_message_signup_incorrectCredentials
        else
            R.string.auth_message_login_incorrectCredentials
        setError(uiTextOf(message))
    }
}

context(ViewModel)
inline val ProfileRepository.profileState get() = profile.stateIn(null)