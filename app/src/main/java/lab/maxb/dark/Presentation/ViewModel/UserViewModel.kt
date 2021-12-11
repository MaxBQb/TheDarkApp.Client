package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Model.Profile3
import lab.maxb.dark.Presentation.Extra.SessionHolder
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Network.OAUTH.Google.GoogleSignInLogic


abstract class UserViewModel: ViewModel() {
    abstract val user: LiveData<Profile3?>
    abstract fun authorize(login: String, password: String): LiveData<Profile3?>
    abstract fun authorizeBySession(authCode: String?): LiveData<Profile3?>
    abstract fun authorizeByOAUTHProvider(login: String, name: String, authCode: String): LiveData<Profile3>
    abstract fun signOut(): Job
}

class UserViewModelImpl(
    private val profileRepository: ProfileRepository,
    private val sessionHolder: SessionHolder,
    private val mGoogleSignInLogic: GoogleSignInLogic,
): UserViewModel() {
    override val user = MutableLiveData<Profile3?>()

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
            sessionHolder.login ?: run {
                emit(null)
                user.postValue(null)
                return@liveData
            }
            emit(profileRepository.getProfile().first().also {
                user.postValue(it)
            })
        }

    override fun authorizeByOAUTHProvider(login: String, name: String, authCode: String)
        = TODO()

    override fun signOut() = viewModelScope.launch {
        mGoogleSignInLogic.signOut()
        sessionHolder.token = null
        sessionHolder.login = null
    }
}