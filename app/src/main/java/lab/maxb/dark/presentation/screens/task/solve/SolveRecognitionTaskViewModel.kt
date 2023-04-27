package lab.maxb.dark.presentation.screens.task.solve

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.usecase.task.GetRecognitionTaskImageUseCase
import lab.maxb.dark.domain.usecase.task.GetRecognitionTaskUseCase
import lab.maxb.dark.domain.usecase.task.MarkRecognitionTaskUseCase
import lab.maxb.dark.domain.usecase.task.SolveRecognitionTaskUseCase
import lab.maxb.dark.presentation.extra.*
import lab.maxb.dark.domain.usecase.profile.GetProfileUseCase
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel


@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class SolveRecognitionTaskViewModel(
    private val getRecognitionTaskImageUseCase: GetRecognitionTaskImageUseCase,
    private val solveRecognitionTaskUseCase: SolveRecognitionTaskUseCase,
    private val markRecognitionTaskUseCase: MarkRecognitionTaskUseCase,
    getProfileUseCase: GetProfileUseCase,
    getRecognitionTaskUseCase: GetRecognitionTaskUseCase,
) : BaseViewModel<TaskSolveUiState, TaskSolveUiEvent>, ViewModel() {
    private val taskId = MutableStateFlow("")
    private val profile = getProfileUseCase().stateInAsResult()
    private val task = taskId.filter { it.isNotEmpty() }.flatMapLatest {
        getRecognitionTaskUseCase(it).asResult()
    }.stateIn()

    private val _uiState = MutableStateFlow(TaskSolveUiState())
    override val uiState = combine(_uiState, profile, task) { state, profileResult, taskResult ->
        val profile = profileResult.valueOrNull
        val task = taskResult.valueOrNull
        state.copy(
            isLoading = anyLoading(profileResult, taskResult) || state.isLoading,
            isReviewMode = isReviewMode(profile),
            isReviewed = task?.reviewed ?: false,
            images = task?.images?.map { getImage(it) } ?: emptyList(),
            taskNotFound = if (task == null && !taskResult.isLoading)
                TaskSolveUiEvent.NoSuchTask else null,
        )
    }.stateIn(TaskSolveUiState())

    fun init(id: String) {
        taskId.value = id
    }

    override fun onEvent(event: TaskSolveUiEvent): Unit = with(event) {
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
        task.firstOrNull()?.valueOrNull?.let {
            withLoading {
                markRecognitionTaskUseCase(
                    it,
                    isAllowed,
                )
            }
        }
    }

    private fun solveRecognitionTask(answer: String) = launch {
        task.firstOrNull()?.valueOrNull?.let {
            withLoading {
                if (!solveRecognitionTaskUseCase(it.id, answer))
                    showMessage(uiTextOf("Неверно"))
            }
        }
    }

    private fun showMessage(message: UiText) = _uiState.update { state ->
        state.copy(
            userMessages = state.userMessages +
                    TaskSolveUiEvent.UserMessage(message)
        )
    }

    private fun getImage(path: String) = getRecognitionTaskImageUseCase(path)
}