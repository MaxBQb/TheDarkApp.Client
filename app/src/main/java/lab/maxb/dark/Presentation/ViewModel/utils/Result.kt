package lab.maxb.dark.Presentation.ViewModel.utils

sealed class UiState<out T> {
    object Loading : UiState<Nothing>() {
        override fun ifLoading(block: () -> Unit) = block()
    }

    data class Success<T>(val value: T) : UiState<T>() {
        override fun ifSuccess(block: (T) -> Unit) = block(value)
        override fun ifLoaded(block: (T?) -> Unit) = block(value)
    }

    data class Error<T>(val throwable: Throwable?) : UiState<T>() {
        override fun ifError(block: (Throwable?) -> Unit) = block(throwable)
        override fun ifLoaded(block: (T?) -> Unit) = block(null)
    }

    open fun ifSuccess(block: (T) -> Unit) {}
    open fun ifError(block: (Throwable?) -> Unit) {}
    open fun ifLoading(block: () -> Unit) {}
    open fun ifLoaded(block: (T?) -> Unit) {}
}
