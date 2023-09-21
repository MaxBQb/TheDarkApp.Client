package lab.maxb.dark.ui.extra

import kotlinx.coroutines.CancellationException

fun Throwable.throwIfCancellation() {
    if (this is CancellationException)
        throw this
}