package lab.maxb.dark.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lab.maxb.dark.presentation.theme.units.sdp


data class Spacing(
    val extraSmall: Dp,
    val small: Dp,
    val normal: Dp,
    val large: Dp,
    val larger: Dp,
    val extraLarge: Dp,
) {
    val zero = 0.dp
}

@Composable
fun defaultScalableSpacing() = Spacing(
    extraSmall = 4.sdp,
    small = 8.sdp,
    normal = 16.sdp,
    large = 32.sdp,
    larger = 48.sdp,
    extraLarge = 64.sdp,
)

fun defaultSpacing() = Spacing(
    extraSmall = 4.dp,
    small = 8.dp,
    normal = 16.dp,
    large = 32.dp,
    larger = 48.dp,
    extraLarge = 64.dp,
)


val LocalSpacing = compositionLocalOf { defaultSpacing() }

inline val MaterialTheme.spacing
    @ReadOnlyComposable
    @Composable
    get() = LocalSpacing.current
