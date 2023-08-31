package lab.maxb.dark.presentation.screens.task.solve

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.usecase.profile.GetProfileUseCase
import lab.maxb.dark.domain.usecase.task.GetRecognitionTaskImageUseCase
import lab.maxb.dark.domain.usecase.task.GetRecognitionTaskUseCase
import lab.maxb.dark.domain.usecase.task.MarkFavoriteRecognitionTaskUseCase
import lab.maxb.dark.domain.usecase.task.MarkRecognitionTaskUseCase
import lab.maxb.dark.domain.usecase.task.SolveRecognitionTaskUseCase
import lab.maxb.dark.presentation.extra.*
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import lab.maxb.dark.presentation.screens.core.effects.withEffectTriggered
import org.koin.android.annotation.KoinViewModel


@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class SolveRecognitionTaskViewModel(
    private val getRecognitionTaskImageUseCase: GetRecognitionTaskImageUseCase,
    private val solveRecognitionTaskUseCase: SolveRecognitionTaskUseCase,
    private val markRecognitionTaskUseCase: MarkRecognitionTaskUseCase,
    private val markFavoriteRecognitionTaskUseCase: MarkFavoriteRecognitionTaskUseCase,
    getProfileUseCase: GetProfileUseCase,
    getRecognitionTaskUseCase: GetRecognitionTaskUseCase,
) : BaseViewModel<TaskSolveUiState, TaskSolveUiEvent, TaskSolveUiSideEffect>() {
    private val taskId = MutableStateFlow("")
    private val profile = getProfileUseCase().stateInAsResult()
    private val task = taskId.filter { it.isNotEmpty() }.flatMapLatest {
        getRecognitionTaskUseCase(it).asResult()
    }.stateIn()

    override fun getInitialState() = TaskSolveUiState()
    override val uiState = combine(_uiState, profile, task) { state, profileResult, taskResult ->
        val profile = profileResult.valueOrNull
        val task = taskResult.valueOrNull
        state.copy(
            isLoading = anyLoading(profileResult, taskResult) || state.isLoading,
            isReviewMode = isReviewMode(profile),
            isReviewed = task?.reviewed ?: false,
            isFavorite = task?.favorite,
            images = task?.images?.map { getImage(it) } ?: emptyList(),
        ).let {
            if (task == null && !taskResult.isLoading)
                it.withEffectTriggered(TaskSolveUiSideEffect.NoSuchTask)
            else it
        }
    }.stateIn(TaskSolveUiState())

    fun init(id: String) {
        taskId.value = id
    }

    override fun handleEvent(event: TaskSolveUiEvent): Unit = with(event) {
        when (this) {
            is TaskSolveUiEvent.AnswerChanged -> setState {
                it.copy(answer = answer)
            }
            is TaskSolveUiEvent.SubmitTaskSolveSolution
                -> solveRecognitionTask(_uiState.value.answer)
            is TaskSolveUiEvent.MarkChanged -> mark(isAllowed)
            is TaskSolveUiEvent.MarkFavorite -> markFavorite(id, isFavorite)
            is TaskSolveUiEvent.ZoomToggled -> setState { it.copy(zoomEnabled = !zoomEnabled) }
            is TaskSolveUiEvent.EffectConsumed -> handleEffectConsumption(this)
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

    private fun setLoading(isLoading: Boolean = true) = setState { state ->
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

    private fun markFavorite(id: String, isFavorite: Boolean) = launch {
        markFavoriteRecognitionTaskUseCase(
            taskId = id,
            isFavorite = isFavorite,
        )
    }

    private fun solveRecognitionTask(answer: String) = launch {
        task.firstOrNull()?.valueOrNull?.let {
            withLoading {
                if (!solveRecognitionTaskUseCase(it.id, answer))
                    showMessage(uiTextOf("Неверно"))
            }
        }
    }

    private fun showMessage(message: UiText) = setState {
        it.withEffectTriggered(TaskSolveUiSideEffect.UserMessage(message))
    }

    private fun getImage(path: String) = getRecognitionTaskImageUseCase(path)
}