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

@Suppress("UNCHECKED_CAST")
fun <T : UiEffectAwareState> T.copy(sideEffectsHolder: UiSideEffectsHolder): T
    = clone(sideEffectsHolder) as T

fun <S: UiEffectAwareState> S.withEffects(
    builder: (UiSideEffectsHolder) -> UiSideEffectsHolder
) = copy(sideEffectsHolder=builder(sideEffectsHolder))

inline fun <S: UiEffectAwareState, reified E: UiSideEffect> S.withEffect(effect: E)
    = withEffects { it + effect }

fun <S: UiEffectAwareState> S.withoutEffect(key: EffectKey)
    = withEffects { it - key }

inline fun <S: UiEffectAwareState, reified E: UiSideEffect> S.withoutEffect(effect: E)
    = withEffects { it - effect }
