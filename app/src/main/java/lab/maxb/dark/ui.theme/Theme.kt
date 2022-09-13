package lab.maxb.dark.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    primaryContainer = PrimaryVariant,
    onPrimary = LightGray,
//    onPrimaryContainer = LightGray,
    secondary = Secondary,
//    secondaryContainer = SecondaryVariant,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(),
        content = content
    )
}