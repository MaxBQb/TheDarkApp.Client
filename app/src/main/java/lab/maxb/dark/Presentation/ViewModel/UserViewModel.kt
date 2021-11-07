package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Model.Server.Profile
import lab.maxb.dark.Domain.Model.Server.Session
import lab.maxb.dark.Presentation.Extra.SessionHolder
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.SessionRepository
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
    private val sessionRepository: SessionRepository,
    private val sessionHolder: SessionHolder,
    private val mGoogleSignInLogic: GoogleSignInLogic,
): UserViewModel() {
    override val user = MutableLiveData<Profile?>()

    override fun authorize(login: String, password: String) = liveData(viewModelScope.coroutineContext) {
        val profile = profileRepository.getProfile(
            login, Profile.getHash(login, password)
        )
        profile?.let{ Session(it).let { session ->
            sessionRepository.addSession(session)
            sessionHolder.session = session
        }}
        user.postValue(profile)
        emit(profile)
    }

    override fun authorizeBySession(authCode: String?) = liveData(viewModelScope.coroutineContext) {
        sessionHolder.session = sessionRepository.getSession(
            sessionHolder.sessionId ?: "",
            sessionHolder.sessionHash ?: "",
            authCode,
        )
        user.postValue(sessionHolder.session?.profile)
        emit(sessionHolder.session?.profile)
    }

    override fun authorizeByOAUTHProvider(login: String, name: String, authCode: String) = liveData(viewModelScope.coroutineContext) {
        sessionHolder.session = sessionRepository.getSession(
            sessionHolder.sessionId ?: "",
            sessionHolder.sessionHash ?: "",
            authCode,
        )
        sessionHolder.session?.profile?.let {
            user.postValue(it)
            emit(it)
            return@liveData
        }
        var profile = profileRepository.getProfile(
            login, Profile.getHash(login, authCode)
        )
        if (profile == null) {
            profile = Profile(login, name, 0, password = authCode)
            profileRepository.addProfile(profile)
        }
        val session = Session(profile, auth_code = authCode)
        sessionRepository.addSession(session)
        sessionHolder.session = session
        user.postValue(profile)
        emit(profile)
    }

    override fun signOut() = viewModelScope.launch {
        mGoogleSignInLogic.signOut()
        sessionRepository.deleteSession(sessionHolder.session ?: return@launch)
        sessionHolder.session = null
    }
}