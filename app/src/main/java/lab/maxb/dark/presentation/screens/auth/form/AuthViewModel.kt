package lab.maxb.dark.presentation.screens.auth.form

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.usecase.auth.AuthorizeUseCase
import lab.maxb.dark.domain.usecase.settings.locale.ChangeLocaleUseCase
import lab.maxb.dark.domain.usecase.settings.locale.GetCurrentLocaleUseCase
import lab.maxb.dark.presentation.extra.FirstOnly
import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.extra.isNotEmpty
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.extra.uiTextOf
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class AuthViewModel(
    private val authorizeUseCase: AuthorizeUseCase,
    getCurrentLocaleUseCase: GetCurrentLocaleUseCase,
    private val changeLocaleUseCase: ChangeLocaleUseCase,
) : BaseViewModel<AuthUiState, AuthUiEvent>, ViewModel() {
    private var authRequest by FirstOnly()

    private val _uiState = MutableStateFlow(AuthUiState())
    override val uiState = combine(_uiState, getCurrentLocaleUseCase()) { state, locale ->
        state.copy(locale=locale)
    }.stateIn(AuthUiState())

    override fun onEvent(event: AuthUiEvent) = with(event) {
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
            is AuthUiEvent.Error -> _uiState.update {
                it.copy(errors = it.errors - this)
            }
            is AuthUiEvent.Authorized -> _uiState.update {
                it.copy(authorized = null)
            }
            is AuthUiEvent.LocaleChanged -> changeLocale(locale)
            is AuthUiEvent.LocaleUpdated -> _uiState.update {
                it.copy(localeUpdated = null)
            }
        }
    }

    private fun setLoading() = _uiState.update {
        it.copy(isLoading = true)
    }

    private fun setError(error: UiText) = _uiState.update {
        it.withError(error)
    }

    private fun AuthUiState.withError(error: UiText) = copy(
        isLoading = false,
        errors = errors + AuthUiEvent.Error(error)
    )

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
        setLoading()
        authRequest = launch {
            val profile = authorizeUseCase(
                AuthCredentials(
                    state.login,
                    state.password,
                    state.isAccountNew,
                )
            )
            handleAuthResult(profile)
        }
    }

    private fun hasEmptyFields() = with(uiState.value) {
        login.isEmpty() || password.isEmpty()
        || isAccountNew && passwordRepeat.isEmpty()
    }

    private fun isPasswordsNotMatch() = with(uiState.value) {
        isAccountNew && password != passwordRepeat
    }

    private fun handleAuthResult(profile: Profile?) = _uiState.update { state ->
        if (!state.isLoading)
            return@update state
        profile?.let {
            return@update AuthUiState(
                login = it.login,
                authorized = AuthUiEvent.Authorized,
            )
        }
        val message = if (state.isAccountNew)
            R.string.auth_message_signup_incorrectCredentials
        else
            R.string.auth_message_login_incorrectCredentials
        state.withError(uiTextOf(message))
    }

    private fun changeLocale(locale: String) {
        launch {
            val newLocale = changeLocaleUseCase(locale)
            _uiState.update {
                it.copy(localeUpdated = AuthUiEvent.LocaleUpdated(newLocale))
            }
        }
    }
}
