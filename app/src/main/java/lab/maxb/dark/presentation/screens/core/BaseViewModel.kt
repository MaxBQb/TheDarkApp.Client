package lab.maxb.dark.presentation.screens.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.screens.core.effects.UiEffectAwareState
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffect
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectConsumed
import lab.maxb.dark.presentation.screens.core.effects.withEffect
import lab.maxb.dark.presentation.screens.core.effects.withoutEffect


abstract class StatefulViewModel<S : UiState> : ViewModel() {
    private val _initialState: S by lazy { getInitialState() }
    protected val _uiState = MutableStateFlow(_initialState)
    open val uiState = _uiState.asStateFlow()
    protected abstract fun getInitialState(): S
    protected inline fun setState(function: (S) -> S) = _uiState.update(function)
}

abstract class PureInteractiveViewModel<S : UiState, E : UiEvent> : StatefulViewModel<S>() {
    private val _uiEvent = MutableSharedFlow<E>()
    fun onEvent(event: E) {
        launch { _uiEvent.emit(event) }
    }

    protected abstract fun handleEvent(event: E)
    protected fun startEventsHandling() = launch {
        _uiEvent.collect {
            handleEvent(it)
        }
    }

    init {
        startEventsHandling()
    }
}

abstract class BaseViewModel<S : UiEffectAwareState, E : UiEvent, SE: UiSideEffect> :
    PureInteractiveViewModel<S, E>() {

    protected fun handleEffectConsumption(
        event: UiSideEffectConsumed
    ) = setState { it.withoutEffect(event.effect) }

    protected inline fun <reified E: SE> setEffect(effect: () -> E)
        = setState { it.withEffect(effect()) }
}
