package lab.maxb.dark.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf


data class AnimationDuration(
    val extraShort: Int,
    val short: Int,
    val medium: Int,
    val long: Int,
    val longer: Int,
    val extraLong: Int,
    val default: Int = medium
) {
    val immediate = 0
}

fun defaultAnimationDuration() = AnimationDuration(
    extraShort = 55,
    short = 110,
    medium = 220,
    long = 440,
    longer = 660,
    extraLong = 880
)


val LocalAnimationDurations = compositionLocalOf { defaultAnimationDuration() }

inline val MaterialTheme.animationDurations
    @ReadOnlyComposable
    @Composable
    get() = LocalAnimationDurations.current
