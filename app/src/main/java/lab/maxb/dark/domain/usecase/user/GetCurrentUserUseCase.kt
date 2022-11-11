package lab.maxb.dark.domain.usecase.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import lab.maxb.dark.domain.repository.ProfileRepository
import lab.maxb.dark.domain.repository.UsersRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class GetCurrentUserUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val usersRepository: UsersRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    open operator fun invoke() = profileRepository.profile
        .filterNotNull().flatMapLatest {
            usersRepository.userResource.query(it.userId, useCache = true)
        }
}