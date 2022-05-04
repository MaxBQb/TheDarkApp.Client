package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Model.Profile
import lab.maxb.dark.Presentation.Extra.UserSettings
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Network.OAUTH.Google.GoogleSignInLogic
import org.koin.android.annotation.KoinViewModel


@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class UserViewModel(
    private val profileRepository: ProfileRepository,
    private val usersRepository: UsersRepository,
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val userSettings: UserSettings,
    private val mGoogleSignInLogic: GoogleSignInLogic,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _profile = MutableStateFlow(UiState.Loading as UiState<Profile?>)
    val profile = _profile.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        viewModelScope.launch {
            profileRepository.profile.mapLatest {
                UiState.Success(it)
            }.catch {
                UiState.Error<Profile?>(it)
            }.also {
                _profile.emitAll(it)
            }
        }
    }

    suspend fun authorize(login: String, password: String) {
        try {
            _profile.value = UiState.Loading
//            withTimeout(Duration.ofSeconds(10).toMillis()) {
                profileRepository.sendCredentials(login, password)
//            }
        } catch (e: Throwable) {
            _profile.value = UiState.Error(e)
            println(e)
        }
    }

    fun authorizeByOAUTHProvider(login: String, name: String, authCode: String) {
        TODO()
    }

    fun signOut() = viewModelScope.launch {
//        mGoogleSignInLogic.signOut()
        userSettings.token = ""
        userSettings.login = ""
        profileRepository.clearCache()
        usersRepository.clearCache()
        recognitionTasksRepository.clearCache()
    }
}

inline fun UiState<Profile?>.ifHasProfile(crossinline block: (Profile) -> Unit) = ifLoaded {
    block(it ?: return@ifLoaded)
}
