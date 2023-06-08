package lab.maxb.dark.presentation.extra

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
inline fun <reified T> synchronizeChanges(
    value: T, crossinline onChanged: (T) -> Unit
): Pair<T, (T) -> Unit> {
    var state by remember(value) { mutableStateOf(value) }
    return state to {
        state = it
        onChanged(state)
    }
}
