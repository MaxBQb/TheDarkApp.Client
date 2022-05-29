package lab.maxb.dark.presentation.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.viewModel.utils.UiState
import lab.maxb.dark.presentation.viewModel.utils.stateIn
import lab.maxb.dark.presentation.viewModel.utils.valueOrNull
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class SolveRecognitionTaskViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val usersRepository: UsersRepository,
    profileRepository: ProfileRepository,
) : ViewModel() {
    private val _id = MutableStateFlow<String?>(null)
    private val profile = profileRepository.profileState
    val answer = MutableStateFlow("")

    fun init(id: String) {
        if (_id.value == id) return
        _id.value = id
        answer.value = ""
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val recognitionTask = _id.filterNotNull().flatMapLatest { merge(
        flowOf(UiState.Loading),
        recognitionTasksRepository.getRecognitionTask(it).mapLatest { task ->
            UiState.Success(task)
        }
    ) }.stateIn(UiState.Loading)

    suspend fun getCurrentTask() = recognitionTask.firstOrNull()?.valueOrNull

    @OptIn(ExperimentalCoroutinesApi::class)
    val isReviewMode get() = profile.mapLatest {
        when (it?.role) {
            Role.MODERATOR, Role.ADMINISTRATOR -> true
            else -> false
        }
    }.stateIn(false)

    suspend fun mark(isAllowed: Boolean) {
        (getCurrentTask() ?: return).apply {
            reviewed = isAllowed
        }.also {
            recognitionTasksRepository.markRecognitionTask(it)
        }
    }

    suspend fun solveRecognitionTask() = getCurrentTask()?.let {
        recognitionTasksRepository.solveRecognitionTask(
            it.id, answer.firstOrNull() ?: ""
        ).also { result ->
            if (result) {
                usersRepository.getUser(profile.firstOrNull()!!.user!!.id, true).firstOrNull()
                recognitionTasksRepository.getRecognitionTask(it.id, true).firstOrNull()
            }
        }
    } ?: false

    fun getImage(path: String) = recognitionTasksRepository.getRecognitionTaskImage(path)
}