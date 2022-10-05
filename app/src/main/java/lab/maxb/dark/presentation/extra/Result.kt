package lab.maxb.dark.presentation.extra

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

// TODO: Drop `UiState` cause it's wrong naming for Result
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

val<T> UiState<T>.valueOrNull get() = when (this) {
    is UiState.Success<T> -> value
    else -> null
}

sealed interface Result<out T> {
    data class Success<T>(val value: T) : Result<T>
    data class Reloading<T>(val value: T?) : Result<T>
    data class Error(val throwable: Throwable?) : Result<Nothing>
    object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}


context(ViewModel)
fun<T> Flow<Result<T>>.stateIn() = stateIn(Result.Loading)

context(ViewModel)
fun<T> Flow<T>.stateInAsResult() = asResult().stateIn()


val<T> Result<T>.isLoaded get() = this is Result.Success<T> || this is Result.Error

val<T> Result<T>.valueOrNull get() = when (this) {
    is Result.Success<T> -> value
    is Result.Reloading<T> -> value
    else -> null
}