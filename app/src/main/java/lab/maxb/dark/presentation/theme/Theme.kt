package lab.maxb.dark.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    primaryContainer = PrimaryVariant,
    onPrimary = LightGray,
//    onPrimaryContainer = LightGray,
    secondary = Secondary,
    secondaryContainer = SecondaryVariant,
    onSecondary = Light,
//    onSecondaryContainer = Light,
//    background = Background,
//    onBackground = LightGray,
//    surface = Background,
//    onSurface = Gray,
)

@Composable
fun DarkAppTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            dynamicDarkColorScheme(LocalContext.current)
        else DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    val materialColors = androidx.compose.material.darkColors(
        surface = colorScheme.background,
        background = colorScheme.background,
        secondaryVariant = colorScheme.background,
        primary = colorScheme.onBackground,
    )
    CompositionLocalProvider(
        LocalSpacing provides defaultScalableSpacing(),
        LocalFontSize provides defaultScalableFontSize(),
        LocalAnimationDurations provides defaultAnimationDuration(),
    ) {
        androidx.compose.material.MaterialTheme(
            colors = materialColors,
        ) {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = typography,
                content = content
            )
        }
    }
}