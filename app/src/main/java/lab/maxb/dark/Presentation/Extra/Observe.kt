package lab.maxb.dark.Presentation.Extra

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> Fragment.observe(livedata: LiveData<T>, observer: Observer<T>)
    = livedata.observe(viewLifecycleOwner, observer)

fun <T> FragmentActivity.observe(livedata: LiveData<T>, observer: Observer<T>)
    = livedata.observe(this, observer)

fun <T> MutableLiveData<T>.set(value: T) = postValue(value)

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun <T> Fragment.observeOnce(livedata: LiveData<T>, observer: Observer<T>)
    = livedata.observeOnce(viewLifecycleOwner, observer)

fun <T> FragmentActivity.observeOnce(livedata: LiveData<T>, observer: Observer<T>)
    = livedata.observeOnce(this, observer)

fun Fragment.launch(block: suspend CoroutineScope.() -> Unit)
    = viewLifecycleOwner.lifecycleScope.launch {
        block()
    }

fun FragmentActivity.launch(block: suspend CoroutineScope.() -> Unit)
    = lifecycleScope.launch {
        block()
    }

fun Fragment.launchRepeatingOnLifecycle(block: suspend CoroutineScope.() -> Unit) = launch {
    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
}

fun FragmentActivity.launchRepeatingOnLifecycle(block: suspend CoroutineScope.() -> Unit) = launch {
    repeatOnLifecycle(Lifecycle.State.STARTED, block)
}

fun <T> Fragment.observe(stateFlow: StateFlow<T>, action: suspend (value: T) -> Unit)
    = launchRepeatingOnLifecycle {
    stateFlow.collectLatest(action)
}

fun <T> FragmentActivity.observe(stateFlow: StateFlow<T>, action: suspend (value: T) -> Unit)
    = launchRepeatingOnLifecycle {
    stateFlow.collectLatest(action)
}

