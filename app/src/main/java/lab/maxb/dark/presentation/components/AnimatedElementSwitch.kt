package lab.maxb.dark.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable

@Composable
fun AnimatedElementSwitch(
    showFirst: Boolean,
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
) {
    AnimatedVisibility(showFirst, enter = fadeIn(), exit = fadeOut()) {
        first()
    }
    AnimatedVisibility(!showFirst, enter = fadeIn(), exit = fadeOut()) {
        second()
    }
}
