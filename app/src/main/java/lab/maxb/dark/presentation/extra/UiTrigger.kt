package lab.maxb.dark.presentation.extra

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import lab.maxb.dark.domain.operations.randomUUID

// UiTrigger is a way to emit/handle one-off UI events
// Emitted by ViewModel, transported with uiState
// Cause same event back from UI to VM when consumed
// And finally resets

abstract class UiTrigger {
    val id = randomUUID
}


data class UiTriggers<T : UiTrigger>(
    private val stateQueue: List<T> = emptyList()
) {
    val state get() = stateQueue.lastOrNull()

    fun add(state: T) = UiTriggers(
        stateQueue + state
    )

    fun remove(state: T) = UiTriggers(
        stateQueue.filter { it.id != state.id }
    )

    operator fun plus(state: T) = add(state)
    operator fun minus(state: T) = remove(state)
}


@Composable
fun <T : UiTrigger> T?.ChangedEffect(
    vararg keys: Any?,
    onConsumed: (T) -> Unit,
    block: suspend context(Context, CoroutineScope) (T) -> Unit,
) = this?.let {
    val context = LocalContext.current.applicationContext
    LaunchedEffect(it, *keys) {
        try {
            block(context, this, it)
        } finally {
            onConsumed(it)
        }
    }
}


@Composable
fun <T : UiTrigger> UiTriggers<T>.ChangedEffect(
    vararg keys: Any?,
    onConsumed: (T) -> Unit,
    block: suspend context(Context, CoroutineScope) (T) -> Unit,
) = state.ChangedEffect(
    keys = keys,
    onConsumed = onConsumed,
    block = block,
)