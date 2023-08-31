package lab.maxb.dark.presentation.screens.core.effects

import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState

interface UiSideEffect

interface UiSideEffectConsumed : UiEvent {
    val effect: EffectKey
}

interface UiEffectAwareState : UiState {
    val sideEffectsHolder: UiSideEffectsHolder
    fun clone(sideEffectsHolder: UiSideEffectsHolder): UiEffectAwareState
}

inline fun <reified T : UiEffectAwareState> T.copy(sideEffectsHolder: UiSideEffectsHolder): T
    = clone(sideEffectsHolder) as T

inline fun <reified S: UiEffectAwareState, reified E: UiSideEffect> S.withEffectTriggered(effect: E)
    = copy(sideEffectsHolder.trigger(effect))

inline fun <reified S: UiEffectAwareState> S.withEffectConsumed(key: EffectKey)
    = copy(sideEffectsHolder.consume(key))

inline fun <reified S: UiEffectAwareState, reified E: UiSideEffect> S.withEffectConsumed(effect: E)
    = copy(sideEffectsHolder.consume(effect))