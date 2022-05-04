package lab.maxb.dark.Presentation.Extra

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private val LifecycleOwner.forObservation get() = when(this) {
    is Fragment -> viewLifecycleOwner
    else -> this
}

context(LifecycleOwner)
@JvmName("observeT")
infix fun <T> LiveData<T>.observe(observer: Observer<T>)
    = observe(forObservation, observer)

fun LifecycleOwner.launch(block: suspend CoroutineScope.() -> Unit){
    forObservation.lifecycleScope.launch {
        block()
    }
}

fun FragmentActivity.launch(block: suspend CoroutineScope.() -> Unit)
    = lifecycleScope.launch {
        block()
    }

fun ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(context, start, block)

fun LifecycleOwner.launchRepeatingOnLifecycle(block: suspend CoroutineScope.() -> Unit) = launch {
    forObservation.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
}

context(LifecycleOwner)
infix fun <T> StateFlow<T>.observe(action: suspend (value: T) -> Unit)
    = launchRepeatingOnLifecycle { collectLatest(action) }
