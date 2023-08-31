package lab.maxb.dark.presentation.extra

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class UiText {
    class Text(val text: String): UiText()
    class TextResource(@StringRes val id: Int, vararg val formatArgs: Any): UiText()
    object Empty: UiText()

    fun asString(context: Context) = when (this) {
        is Text -> text
        is TextResource -> context.resources.getString(id, *formatArgs)
        is Empty -> ""
    }
}

fun uiTextOf(value: String) = UiText.Text(value)

fun uiTextOf(@StringRes id: Int, vararg formatArgs: Any)
        = UiText.TextResource(id, *formatArgs)

@Composable
fun UiText.asString() = asString(LocalContext.current)

context(Context)
fun UiText.asString() = asString(this@Context)

inline val UiText.isEmpty get() = this is UiText.Empty
inline val UiText.isNotEmpty get() = this !is UiText.Empty

suspend fun UiText.show(state: SnackbarHostState, context: Context)
        = state.showSnackbar(asString(context))
