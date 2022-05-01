package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Model.Profile
import lab.maxb.dark.Presentation.Extra.UserSettings
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Network.OAUTH.Google.GoogleSignInLogic


abstract class UserViewModel: ViewModel() {
    abstract val user: LiveData<Profile?>
    abstract fun authorize(login: String, password: String): LiveData<Profile?>
    abstract fun authorizeBySession(authCode: String?): LiveData<Profile?>
    abstract fun authorizeByOAUTHProvider(login: String, name: String, authCode: String): LiveData<Profile>
    abstract fun signOut(): Job
}

class UserViewModelImpl(
    private val profileRepository: ProfileRepository,
    private val usersRepository: UsersRepository,
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val userSettings: UserSettings,
    private val mGoogleSignInLogic: GoogleSignInLogic,
): UserViewModel() {
    override val user = MutableLiveData<Profile?>()

    override fun authorize(login: String, password: String)
        = liveData(viewModelScope.coroutineContext) {
            emit((try {
                profileRepository.getProfile(
                    login, password
                ).first()
            } catch (e: Throwable) { null }).also {
                user.postValue(it)
            })
        }

    override fun authorizeBySession(authCode: String?)
        = liveData(viewModelScope.coroutineContext) {
            emit((try {
                profileRepository.getProfile().first()
            } catch (e: Throwable) { null }).also {
                user.postValue(it)
            })
        }

    override fun authorizeByOAUTHProvider(login: String, name: String, authCode: String)
        = TODO()

    override fun signOut() = viewModelScope.launch {
        mGoogleSignInLogic.signOut()
        userSettings.token = ""
        userSettings.login = ""
        profileRepository.clearCache()
        usersRepository.clearCache()
        recognitionTasksRepository.clearCache()
    }
}