package lab.maxb.dark.presentation.screens.auth.form

import kotlinx.coroutines.flow.combine
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
import lab.maxb.dark.presentation.screens.core.effects.withEffectTriggered
import org.koin.android.annotation.KoinViewModel
import lab.maxb.dark.presentation.screens.auth.form.AuthUiContract as Ui


@KoinViewModel
class AuthViewModel(
    private val authorizeUseCase: AuthorizeUseCase,
    getCurrentLocaleUseCase: GetCurrentLocaleUseCase,
    private val changeLocaleUseCase: ChangeLocaleUseCase,
) : BaseViewModel<Ui.State, Ui.Event, Ui.SideEffect>() {
    private var authRequest by FirstOnly()

    override fun getInitialState() = Ui.State()
    override val uiState = combine(_uiState, getCurrentLocaleUseCase()) { state, locale ->
        state.copy(locale=locale)
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

    private fun setLoading() = setState {
        it.copy(isLoading = true)
    }

    private fun Ui.State.withError(error: UiText) = copy(
        isLoading = false,
    ).withEffectTriggered(Ui.SideEffect.Error(error))

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

    private fun handleAuthResult(profile: Profile?) = setState { state ->
        if (!state.isLoading)
            return@setState state
        profile?.let {
            return@setState Ui.State(
                login = it.login,
            ).withEffectTriggered(Ui.SideEffect.Authorized)
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
            setState {
                it.withEffectTriggered(Ui.SideEffect.LocaleUpdated(newLocale))
            }
        }
    }
}
