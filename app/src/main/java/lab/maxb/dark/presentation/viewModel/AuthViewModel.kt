package lab.maxb.dark.presentation.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withTimeout
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.presentation.extra.UserSettings
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.viewModel.utils.UiState
import lab.maxb.dark.presentation.viewModel.utils.stateIn
import org.koin.android.annotation.KoinViewModel
import java.time.Duration


@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class AuthViewModel(
    private val profileRepository: ProfileRepository,
    private val db: LocalDatabase,
    private val userSettings: UserSettings,
//    private val mGoogleSignInLogic: GoogleSignInLogic,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _profile = MutableStateFlow(UiState.Loading as UiState<Profile?>)
    val profile = _profile.stateIn(UiState.Loading)

    val login = MutableStateFlow("")
    val password = MutableStateFlow("")
    val showPassword = MutableStateFlow(false)
    val passwordRepeat = MutableStateFlow("")
    val isAccountNew = MutableStateFlow(false)
    val isLoading = MutableStateFlow(false)

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

    suspend fun authorize() {
        try {
            _profile.value = UiState.Loading
            withTimeout(Duration.ofSeconds(10).toMillis()) {
                profileRepository.sendCredentials(login.value, password.value, isAccountNew.value)
            }
        } catch (e: Throwable) {
            _profile.value = UiState.Error(e)
            println(e)
        }
    }

    fun authorizeByOAUTHProvider(login: String, name: String, authCode: String) {
        TODO()
    }

    fun hasEmptyFields() =
        login.value.isEmpty() ||
        password.value.isEmpty() ||
        isAccountNew.value && passwordRepeat.value.isEmpty()

    fun isPasswordsNotMatch() = isAccountNew.value && password.value != passwordRepeat.value

    fun signOut() = launch(Dispatchers.Default) {
//        mGoogleSignInLogic.signOut()
        userSettings.token = ""
        userSettings.login = ""
        isLoading.value = false
        db.clearAllTables()
    }
}

context(ViewModel)
inline val ProfileRepository.profileState get() = profile.stateIn(null)