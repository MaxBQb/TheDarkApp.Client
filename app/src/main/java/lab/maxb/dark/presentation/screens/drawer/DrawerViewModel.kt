package lab.maxb.dark.presentation.screens.drawer

import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.usecase.task.GetFavoriteRecognitionTaskListUseCase
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.screens.core.StatefulViewModel
import org.koin.android.annotation.KoinViewModel
import lab.maxb.dark.presentation.screens.drawer.DrawerUiContract as Ui


@KoinViewModel
class DrawerViewModel(
    favoriteRecognitionTaskListUseCase: GetFavoriteRecognitionTaskListUseCase,
) : StatefulViewModel<Ui.State>() {
    override fun getInitialState() = Ui.State()

    private val _canObtainFavorites = favoriteRecognitionTaskListUseCase.canBeObtained().stateIn(false)
    override val uiState = combine(_uiState, _canObtainFavorites) { state, canObtainFavorites ->
        state.copy(
            destinations = getDestinations(canObtainFavorites)
        )
    }.stateIn(Ui.State())

    private fun getDestinations(canObtainFavorites: Boolean) =
        DrawerDestination.values().toList().let { destinations ->
            if (!canObtainFavorites)
                destinations.filter { it.name != DrawerDestination.FavoriteTasks.name }
            else
                destinations
        }
}
