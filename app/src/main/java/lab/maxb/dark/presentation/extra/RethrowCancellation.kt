package lab.maxb.dark.presentation.extra

import kotlinx.coroutines.CancellationException

fun Throwable.throwIfCancellation() {
    if (this is CancellationException)
        throw this
}