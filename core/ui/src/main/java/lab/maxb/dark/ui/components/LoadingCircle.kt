package lab.maxb.dark.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import lab.maxb.dark.ui.theme.units.sdp

@Composable
fun LoadingCircle(modifier: Modifier = Modifier, width: Int = 5) {
    val color = MaterialTheme.colorScheme.onSurface.copy(0.1f)
    val strokeWidth = width.sdp
    if (LocalView.current.isInEditMode)
        CircularProgressIndicator(
            0.45f,
            modifier,
            color,
            strokeWidth,
        )
    else
        CircularProgressIndicator(
            modifier,
            color,
            strokeWidth,
        )
}

@Composable
fun LoadingCircle(size: Float, modifier: Modifier = Modifier, width: Int = 5)
    = LoadingCircle(modifier.fillMaxSize(size / 2f), width)