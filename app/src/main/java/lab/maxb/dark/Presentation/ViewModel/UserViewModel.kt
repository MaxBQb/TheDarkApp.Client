package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withTimeout
import lab.maxb.dark.Domain.Model.Profile
import lab.maxb.dark.Presentation.Extra.UserSettings
import lab.maxb.dark.Presentation.Extra.launch
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Network.OAUTH.Google.GoogleSignInLogic
import lab.maxb.dark.Presentation.ViewModel.utils.UiState
import lab.maxb.dark.Presentation.ViewModel.utils.stateIn
import org.koin.android.annotation.KoinViewModel
import java.time.Duration


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
    val profile = _profile.stateIn(UiState.Loading)

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

    suspend fun authorize(login: String, password: String) {
        try {
            _profile.value = UiState.Loading
            withTimeout(Duration.ofSeconds(10).toMillis()) {
                profileRepository.sendCredentials(login, password)
            }
        } catch (e: Throwable) {
            _profile.value = UiState.Error(e)
            println(e)
        }
    }

    fun authorizeByOAUTHProvider(login: String, name: String, authCode: String) {
        TODO()
    }

    fun signOut() = launch {
//        mGoogleSignInLogic.signOut()
        userSettings.token = ""
        userSettings.login = ""
        profileRepository.clearCache()
        usersRepository.clearCache()
        recognitionTasksRepository.clearCache()
    }
}

context(ViewModel)
inline val ProfileRepository.profileState get() = profile.stateIn(null)