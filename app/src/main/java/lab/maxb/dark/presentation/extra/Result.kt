package lab.maxb.dark.presentation.extra

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import lab.maxb.dark.presentation.screens.core.UiState

sealed interface Result<out T> : UiState {
    data class Success<T>(val value: T) : Result<T>
    data class Error(
        val throwable: Throwable?,
        val message: UiText = uiTextOf(throwable?.message ?: "")
    ) : Result<Nothing>
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

inline val Result<*>.isLoading get()
    = this is Result.Loading

inline val Result<*>.isError get()
    = this is Result.Error

inline val<T> Result<T>.isSuccess get()
    = this is Result.Success<T>

fun<T> anyLoading(vararg args: Result<T>)
    = args.any { it.isLoading }

fun<T> anyError(vararg args: Result<T>)
    = args.any { it.isError }

val<T> Result<T>.valueOrNull get() = when (this) {
    is Result.Success<T> -> value
    else -> null
}

fun<T> Result<T>.require() = valueOrNull!!