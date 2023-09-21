package lab.maxb.dark.ui.extra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


context(ViewModel)
fun<T> Flow<T>.stateIn(initialValue: T) = stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = initialValue
)

class LatestOnly : ReadWriteProperty<Any, Job?> {
    private var value: Job? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): Job? {
        return value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Job?) {
        this.value?.cancel()
        this.value = value
    }
}

class FirstOnly : ReadWriteProperty<Any, Job?> {
    private var value: Job? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): Job? {
        return value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Job?) {
        if (this.value?.isCompleted == true)
            this.value = value
    }
}

fun ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(context, start, block)

context(ViewModel)
fun<T> Flow<Result<T>>.stateIn() = stateIn(Result.Loading)

context(ViewModel)
fun<T> Flow<T>.stateInAsResult() = asResult().stateIn()