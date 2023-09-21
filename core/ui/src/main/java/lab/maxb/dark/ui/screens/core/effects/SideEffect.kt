package lab.maxb.dark.ui.screens.core.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import kotlinx.coroutines.CoroutineScope


@Composable
@NonRestartableComposable
inline fun <reified T: UiSideEffect> SideEffect(
    effects: UiSideEffectsHolder,
    noinline onConsumed: (EffectKey) -> Unit,
    immediate: Boolean = false,
    vararg keys: Any?,
    crossinline action: suspend CoroutineScope.(T) -> Unit,
) = LaunchedEffect(effects, onConsumed, *keys) {
    val effect = effects.get<T>() ?: return@LaunchedEffect
    if (immediate)
        onConsumed(EffectKey<T>())
    action(effect)
    if (!immediate)
        onConsumed(EffectKey<T>())
}

@Immutable
interface SideEffectsScope {
    val effects: UiSideEffectsHolder
    val onConsumed: (EffectKey) -> Unit
}

@Immutable
private data class SideEffectsScopeImpl(
    override val effects: UiSideEffectsHolder,
    override val onConsumed: (EffectKey) -> Unit
): SideEffectsScope

@Composable
@NonRestartableComposable
fun SideEffects(
    effects: UiSideEffectsHolder,
    onConsumed: (EffectKey) -> Unit,
    action: @Composable SideEffectsScope.() -> Unit,
) = action(SideEffectsScopeImpl(effects, onConsumed))

@Composable
@NonRestartableComposable
inline fun <reified T: UiSideEffect> SideEffectsScope.On(
    immediate: Boolean = false,
    vararg keys: Any?,
    crossinline action: suspend CoroutineScope.(T) -> Unit,
) = SideEffect(effects, onConsumed, immediate, *keys, action=action)