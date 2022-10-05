package lab.maxb.dark.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun <T> T.AnimateAppearance(
    modifier: Modifier = Modifier,
    enter: EnterTransition = expandVertically() + fadeIn(),
    exit: ExitTransition = fadeOut() + shrinkVertically(),
    initiallyVisible: Boolean = false,
    content: @Composable T.() -> Unit
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