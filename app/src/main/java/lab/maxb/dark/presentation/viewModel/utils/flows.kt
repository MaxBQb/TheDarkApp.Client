package lab.maxb.dark.presentation.viewModel.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*


context(ViewModel)
fun<T> Flow<T>.stateIn(initialValue: T) = stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = initialValue
)

suspend fun<T: Any> StateFlow<T?>.firstNotNull() = first { it != null }!!