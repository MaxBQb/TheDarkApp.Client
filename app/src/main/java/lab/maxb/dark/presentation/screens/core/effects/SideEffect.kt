package lab.maxb.dark.presentation.screens.core.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable


@Composable
@NonRestartableComposable
inline fun <reified T: UiSideEffect> SideEffect(
    effects: UiSideEffectsHolder,
    noinline onConsumed: (EffectKey) -> Unit,
    immediate: Boolean = false,
    crossinline action: suspend (T) -> Unit,
) = LaunchedEffect(key1 = effects, key2 = onConsumed) {
    val effect = effects.get<T>() ?: return@LaunchedEffect
    if (immediate)
        onConsumed(EffectKey<T>())
    action(effect)
    if (!immediate)
        onConsumed(EffectKey<T>())
}
