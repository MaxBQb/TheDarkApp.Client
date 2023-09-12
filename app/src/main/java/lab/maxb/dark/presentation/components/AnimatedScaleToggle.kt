package lab.maxb.dark.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import lab.maxb.dark.presentation.theme.LocalAnimationDurations


@Composable
fun AnimatedScaleToggle(
    value: Boolean,
    duration: Int = LocalAnimationDurations.current.default,
    content: @Composable (Boolean) -> Unit,
) = AnimatedContent(
    targetState = value,
    contentAlignment = Alignment.Center,
    transitionSpec = {
        val fadeSpec = tween<Float>(duration)
        val scaleSpec = tween<Float>(2 * duration)
        fadeIn(scaleSpec) + scaleIn(fadeSpec) togetherWith
                fadeOut(scaleSpec) + scaleOut(fadeSpec)
    },
    content = { content(it) },
)
