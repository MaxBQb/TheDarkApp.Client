package lab.maxb.dark.presentation.components.utils

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

data class InputOptions(
    val actions: KeyboardActions,
    val options: KeyboardOptions,
    val event: (KeyEvent) -> Boolean,
)


private val goKeys = listOf(Key.Tab, Key.Enter)
val keyboardNext: InputOptions
    @Composable get() {
    val localFocus = LocalFocusManager.current
    return InputOptions(
        actions = KeyboardActions(onNext = {
            localFocus.moveFocus(FocusDirection.Down)
        }),
        options = KeyboardOptions(imeAction = ImeAction.Next),
        event = {
            if (it.key in goKeys && it.nativeKeyEvent.action == android.view.KeyEvent.ACTION_DOWN) {
                localFocus.moveFocus(FocusDirection.Down)
                true
            } else false
        }
    )
}
val keyboardClose: InputOptions
    @Composable get() {
    val localFocus = LocalFocusManager.current
    return InputOptions(
        actions = KeyboardActions(onDone = {
            localFocus.clearFocus()
        }),
        options = KeyboardOptions(imeAction = ImeAction.Done),
        event = {
            if (it.key in goKeys && it.nativeKeyEvent.action == android.view.KeyEvent.ACTION_DOWN) {
                localFocus.clearFocus()
                true
            } else false
        }
    )
}

fun Modifier.withInputOptions(options: InputOptions) = onPreviewKeyEvent(options.event)