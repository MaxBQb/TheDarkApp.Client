package lab.maxb.dark.presentation.extra

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    block: suspend CoroutineScope.(T) -> Unit,
) = this?.let {
    LaunchedEffect(it, *keys) {
        try {
            block(it)
        } finally {
            onConsumed(it)
        }
    }
}


@Composable
fun <T : UiTrigger> UiTriggers<T>.ChangedEffect(
    vararg keys: Any?,
    onConsumed: (T) -> Unit,
    block: suspend CoroutineScope.(T) -> Unit,
) = state.ChangedEffect(
    keys = keys,
    onConsumed = onConsumed,
    block = block,
)