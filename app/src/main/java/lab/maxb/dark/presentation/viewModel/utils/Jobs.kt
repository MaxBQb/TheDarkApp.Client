package lab.maxb.dark.presentation.viewModel.utils

import kotlinx.coroutines.Job
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

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
