package lab.maxb.dark.presentation.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.presentation.extra.*
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.viewModel.utils.*
import org.koin.android.annotation.KoinViewModel


data class TaskSolveUiState(
    val answer: String = "",
    val isLoading: Boolean = false,
    val isReviewMode: Boolean = false,
    val isReviewed: Boolean = false,
    val images: List<Any?> = emptyList(),
    val userMessages: UiTriggers<TaskSolveUiEvent.UserMessage> = UiTriggers(),
    val taskNotFound: TaskSolveUiEvent.NoSuchTask? = null,
)


sealed interface TaskSolveUiEvent {
    data class AnswerChanged(val answer: String) : TaskSolveUiEvent
    object SubmitTaskSolveSolution : TaskSolveUiEvent
    data class MarkChanged(val isAllowed: Boolean) : TaskSolveUiEvent

    // UiTriggers
    data class UserMessage(val message: UiText): UiTrigger(), TaskSolveUiEvent
    object NoSuchTask: UiTrigger(), TaskSolveUiEvent
}


@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class SolveRecognitionTaskViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val usersRepository: UsersRepository,
    profileRepository: ProfileRepository,
) : ViewModel() {
    private val taskId = MutableStateFlow("")
    val shareLink get() = "https://dark/task/${taskId.value}"
    private val profile = profileRepository.profile.stateInAsResult()
    private val task = taskId.filter { it.isNotEmpty() }.flatMapLatest {
        recognitionTasksRepository.getRecognitionTask(it).asResult()
    }.stateIn()

    private val _uiState = MutableStateFlow(TaskSolveUiState())
    val uiState = combine(_uiState, profile, task) { state, profileResult, taskResult ->
        val profile = profileResult.valueOrNull
        val task = taskResult.valueOrNull
        state.copy(
            isLoading = !profileResult.isLoaded || !taskResult.isLoaded || state.isLoading,
            isReviewMode = isReviewMode(profile),
            isReviewed = task?.reviewed ?: false,
            images = task?.images?.map { getImage(it) } ?: emptyList(),
            taskNotFound = if (task == null && taskResult.isLoaded)
                TaskSolveUiEvent.NoSuchTask else null,
        )
    }.stateIn(TaskSolveUiState())

    fun init(id: String) {
        taskId.value = id
    }

    fun onEvent(event: TaskSolveUiEvent): Unit = with(event) {
        when (this) {
            is TaskSolveUiEvent.AnswerChanged -> _uiState.update {
                it.copy(answer = answer)
            }
            is TaskSolveUiEvent.SubmitTaskSolveSolution
            -> solveRecognitionTask(_uiState.value.answer)
            is TaskSolveUiEvent.MarkChanged -> mark(isAllowed)
            is TaskSolveUiEvent.UserMessage -> _uiState.update {
                it.copy(userMessages = it.userMessages - this)
            }
            TaskSolveUiEvent.NoSuchTask -> _uiState.update {
                it.copy(taskNotFound = null)
            }
        }
    }

    private inline fun withLoading(block: () -> Unit) {
        try {
            setLoading(true)
            block()
        } finally {
            setLoading(false)
        }
    }

    private fun setLoading(isLoading: Boolean = true) = _uiState.update { state ->
        state.copy(isLoading = isLoading)
    }

    private fun isReviewMode(profile: Profile?) = when (profile?.role) {
        Role.MODERATOR, Role.ADMINISTRATOR -> true
        else -> false
    }

    private fun mark(isAllowed: Boolean) = launch {
        // TODO: Fix mark not caught properly
        task.firstOrNull()?.valueOrNull?.let { withLoading {
            it.reviewed = isAllowed
            recognitionTasksRepository.markRecognitionTask(it)
        } }
    }

    private fun solveRecognitionTask(answer: String) = launch {
        task.firstOrNull()?.valueOrNull?.let { withLoading {
            recognitionTasksRepository.solveRecognitionTask(
                it.id, answer
            ).also { result ->
                if (result) {
                    usersRepository.refresh(profile.firstOrNull()!!.valueOrNull!!.user!!.id)
                    recognitionTasksRepository.refresh(it.id)
                } else {
                    showMessage(uiTextOf("Неверно"))
                }
            }
        } }
    }

    private fun showMessage(message: UiText) {
        _uiState.update { state ->
            state.copy(
                userMessages = state.userMessages +
                    TaskSolveUiEvent.UserMessage(message)
            )
        }
    }

    private fun getImage(path: String) = recognitionTasksRepository.getRecognitionTaskImage(path)
}