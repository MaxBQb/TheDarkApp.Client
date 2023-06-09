package lab.maxb.dark.presentation.screens.drawer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.usecase.task.GetFavoriteRecognitionTaskListUseCase
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class DrawerViewModel(
    favoriteRecognitionTaskListUseCase: GetFavoriteRecognitionTaskListUseCase,
) : BaseViewModel<DrawerUiState, DrawerUiEvent>, ViewModel() {

    override fun onEvent(event: DrawerUiEvent) {}

    private val _canObtainFavorites = favoriteRecognitionTaskListUseCase.canBeObtained().stateIn(false)
    private val _uiState = MutableStateFlow(DrawerUiState())
    override val uiState = combine(_uiState, _canObtainFavorites) { state, canObtainFavorites ->
        state.copy(
            destinations = getDestinations(canObtainFavorites)
        )
    }.stateIn(DrawerUiState())

    private fun getDestinations(canObtainFavorites: Boolean) =
        DrawerDestination.values().toList().let { destinations ->
            if (!canObtainFavorites)
                destinations.filter { it.name != DrawerDestination.FavoriteTasks.name }
            else
                destinations
        }
}
