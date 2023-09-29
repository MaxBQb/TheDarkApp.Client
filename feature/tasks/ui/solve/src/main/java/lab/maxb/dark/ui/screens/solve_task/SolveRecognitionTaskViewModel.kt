package lab.maxb.dark.ui.screens.solve_task

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
import lab.maxb.dark.ui.extra.*
import lab.maxb.dark.ui.screens.core.BaseViewModel
import lab.maxb.dark.ui.screens.core.effects.withEffect
import org.koin.android.annotation.KoinViewModel
import lab.maxb.dark.ui.screens.solve_task.TaskSolveUiContract as Ui


@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class SolveRecognitionTaskViewModel(
    private val getRecognitionTaskImageUseCase: GetRecognitionTaskImageUseCase,
    private val solveRecognitionTaskUseCase: SolveRecognitionTaskUseCase,
    private val markRecognitionTaskUseCase: MarkRecognitionTaskUseCase,
    private val markFavoriteRecognitionTaskUseCase: MarkFavoriteRecognitionTaskUseCase,
    getProfileUseCase: GetProfileUseCase,
    getRecognitionTaskUseCase: GetRecognitionTaskUseCase,
) : BaseViewModel<Ui.State, Ui.Event, Ui.SideEffect>() {
    private val taskId = MutableStateFlow("")
    private val profile = getProfileUseCase().stateInAsResult()
    private val task = taskId.filter { it.isNotEmpty() }.flatMapLatest {
        getRecognitionTaskUseCase(it).asResult()
    }.stateIn()

    override fun getInitialState() = Ui.State()
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
                it.withEffect(Ui.SideEffect.NoSuchTask)
            else it
        }
    }.stateIn(Ui.State())

    fun init(id: String) {
        taskId.value = id
    }

    override fun handleEvent(event: Ui.Event): Unit = with(event) {
        when (this) {
            is Ui.Event.AnswerChanged -> setState {
                it.copy(answer = answer)
            }
            is Ui.Event.SubmitTaskSolveSolution
                -> solveRecognitionTask(_uiState.value.answer)
            is Ui.Event.MarkChanged -> mark(isAllowed)
            is Ui.Event.MarkFavorite -> markFavorite(id, isFavorite)
            is Ui.Event.ZoomToggled -> setState { it.copy(zoomEnabled = !zoomEnabled) }
            is Ui.Event.EffectConsumed -> handleEffectConsumption(this)
        }
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

    private fun showMessage(message: UiText)
        = setEffect { Ui.SideEffect.UserMessage(message) }

    private fun getImage(path: String) = getRecognitionTaskImageUseCase(path)
}