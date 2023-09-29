package lab.maxb.dark.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import lab.maxb.dark.ui.theme.units.sdp

@Composable
fun LoadingScreen(show: Boolean, alpha: Float = 0.7f) = AnimatedVisibility(
    visible = show,
    enter = fadeIn(),
    exit = fadeOut(),
) {
    Box(
        Modifier
            .fillMaxSize()
            .alpha(alpha)
            .background(MaterialTheme.colorScheme.surface),
        Alignment.Center
    ) {
        LoadingCircle(
            width = 14,
            modifier = Modifier
                .alpha(alpha)
                .size(200.sdp)
        )
    }
}
