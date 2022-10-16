package lab.maxb.dark.domain.usecase.task

import kotlinx.coroutines.flow.firstOrNull
import lab.maxb.dark.domain.repository.ProfileRepository
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import lab.maxb.dark.domain.repository.UsersRepository
import org.koin.core.annotation.Singleton

@Singleton
open class SolveRecognitionTaskUseCase(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val usersRepository: UsersRepository,
    private val profileRepository: ProfileRepository,
) {
    open suspend operator fun invoke(id: String, answer: String)
        = recognitionTasksRepository.solveRecognitionTask(id, answer).also {
        if (!it) return@also
        usersRepository.refresh(profileRepository.profile.firstOrNull()!!.user!!.id)
        recognitionTasksRepository.refresh(id)
    }

}