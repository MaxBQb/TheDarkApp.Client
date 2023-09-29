package lab.maxb.dark.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun AnimateAppearance(
    modifier: Modifier = Modifier,
    enter: EnterTransition = expandVertically() + fadeIn(),
    exit: ExitTransition = fadeOut() + shrinkVertically(),
    initiallyVisible: Boolean = false,
    content: @Composable () -> Unit
) {
    val state = remember {
        MutableTransitionState(initiallyVisible).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visibleState = state,
        enter = enter,
        exit = exit
    ) { content() }
}