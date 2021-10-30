package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import lab.maxb.dark.Domain.Model.Server.Profile
import lab.maxb.dark.Domain.Model.Server.Session
import lab.maxb.dark.Presentation.Extra.SessionHolder
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.SessionRepository


class LoginViewModel(
    private val profileRepository: ProfileRepository,
    private val sessionRepository: SessionRepository,
    private val sessionHolder: SessionHolder,
) : ViewModel() {
    fun authorize(login: String, password: String) = liveData(viewModelScope.coroutineContext) {
        val profile = profileRepository.getProfile(
            login, Profile.getHash(login, password)
        )
        profile?.let{Session(it).let { session ->
            sessionRepository.addSession(session)
            sessionHolder.saveSession(session)
        }}
        emit(profile)
    }

    fun authorizeBySession() = liveData(viewModelScope.coroutineContext) {
        emit(sessionRepository.getSession(
            sessionHolder.sessionId ?: "",
            sessionHolder.sessionHash ?: ""
        )?.profile)
    }
}