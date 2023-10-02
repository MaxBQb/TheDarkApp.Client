package lab.maxb.dark.ui.screens.auth_form

import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.usecase.AuthorizeUseCase
import lab.maxb.dark.domain.usecase.ChangeLocaleUseCase
import lab.maxb.dark.domain.usecase.GetCurrentLocaleUseCase
import lab.maxb.dark.ui.auth.R
import lab.maxb.dark.ui.extra.FirstOnly
import lab.maxb.dark.ui.extra.UiText
import lab.maxb.dark.ui.extra.isNotEmpty
import lab.maxb.dark.ui.extra.launch
import lab.maxb.dark.ui.extra.stateIn
import lab.maxb.dark.ui.extra.uiTextOf
import lab.maxb.dark.ui.screens.common.Loaded
import lab.maxb.dark.ui.screens.common.Loading
import lab.maxb.dark.ui.screens.core.BaseViewModel
import lab.maxb.dark.ui.screens.core.effects.withEffect
import org.koin.android.annotation.KoinViewModel
import lab.maxb.dark.ui.screens.auth_form.AuthUiContract as Ui


@KoinViewModel
class AuthViewModel(
    private val authorizeUseCase: AuthorizeUseCase,
    getCurrentLocaleUseCase: GetCurrentLocaleUseCase,
    private val changeLocaleUseCase: ChangeLocaleUseCase,
) : BaseViewModel<Ui.State, Ui.Event, Ui.SideEffect>() {
    private var authRequest by FirstOnly()

    override fun getInitialState() = Ui.State()
    override val uiState = combine(_uiState, getCurrentLocaleUseCase()) { state, locale ->
        state.copy(locale = locale)
    }.stateIn(Ui.State())

    override fun handleEvent(event: Ui.Event) = with(event) {
        when (this) {
            is Ui.Event.LoginChanged -> setState {
                it.copy(login = login)
            }

            is Ui.Event.PasswordChanged -> setState {
                it.copy(password = password)
            }

            is Ui.Event.PasswordRepeatChanged -> setState {
                it.copy(passwordRepeat = password)
            }

            is Ui.Event.PasswordVisibilityChanged -> setState {
                it.copy(showPassword = showPassword)
            }

            is Ui.Event.RegistrationNeededChanged -> setState {
                it.copy(isAccountNew = isAccountNew)
            }

            is Ui.Event.Submit -> authorize()
            is Ui.Event.LocaleChanged -> changeLocale(locale)
            is Ui.Event.EffectConsumed -> handleEffectConsumption(this)
        }
    }

    private fun Ui.State.withError(error: UiText)
        = Loaded.withEffect(Ui.SideEffect.Error(error))

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
            return setState { it.withError(error) }
        setState { it.Loading }
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

    private fun handleAuthResult(profile: Profile?) = setState { state ->
        if (!state.isLoading)
            return@setState state
        profile?.let {
            return@setState Ui.State(
                login = it.login,
            ).withEffect(Ui.SideEffect.Authorized)
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
            setEffect { Ui.SideEffect.LocaleUpdated(newLocale) }
        }
    }
}
