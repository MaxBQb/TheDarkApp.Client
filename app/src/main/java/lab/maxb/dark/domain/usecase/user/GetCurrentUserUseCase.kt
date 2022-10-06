package lab.maxb.dark.domain.usecase.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import lab.maxb.dark.domain.repository.ProfileRepository
import lab.maxb.dark.domain.repository.UsersRepository
import org.koin.core.annotation.Singleton

@Singleton
class GetCurrentUserUseCase(
    private val profileRepository: ProfileRepository,
    private val usersRepository: UsersRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke() = profileRepository.profile
        .filterNotNull().flatMapLatest {
            usersRepository.getUser(it.user!!.id)
        }
}