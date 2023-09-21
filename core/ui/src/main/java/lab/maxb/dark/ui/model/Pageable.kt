package lab.maxb.dark.ui.model

import androidx.paging.PagingData
import lab.maxb.dark.domain.model.LayerSpecific
import lab.maxb.dark.domain.model.Pageable

@Suppress("UNCHECKED_CAST")
fun <T : Any> LayerSpecific<Pageable<T>>.toPresentation(): PagingData<T>
    = data as PagingData<T>

fun <T : Any> PagingData<T>.toDomain() = LayerSpecific<Pageable<T>>(this)

