package lab.maxb.dark.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import lab.maxb.dark.ui.theme.units.ssp


data class FontSize(
    val extraSmall: TextUnit,
    val small: TextUnit,
    val normalBody: TextUnit,
    val normalHeader: TextUnit,
    val large: TextUnit,
    val extraLarge: TextUnit,
) {
    val normal = normalBody
}

@Composable
fun defaultScalableFontSize() = FontSize(
    extraSmall = 8.ssp,
    small = 12.ssp,
    normalBody = 14.ssp,
    normalHeader = 18.ssp,
    large = 24.ssp,
    extraLarge = 48.ssp,
)

fun defaultFontSize() = FontSize(
    extraSmall = 8.sp,
    small = 12.sp,
    normalBody = 14.sp,
    normalHeader = 18.sp,
    large = 24.sp,
    extraLarge = 48.sp,
)


val LocalFontSize = compositionLocalOf { defaultFontSize() }

inline val MaterialTheme.fontSize
    @ReadOnlyComposable
    @Composable
    get() = LocalFontSize.current
