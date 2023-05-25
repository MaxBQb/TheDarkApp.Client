package lab.maxb.dark.data.utils.pagination

import androidx.paging.PagingData
import androidx.paging.map

inline fun <T : Any> PagingData<T>.replace(
    value: T?,
    crossinline compare: suspend (T, T) -> Boolean = { x, y -> x == y }
) = value?.let { x -> map { if (compare(it, x)) x else it } } ?: this
