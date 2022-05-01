package lab.maxb.dark.Presentation.Extra

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

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