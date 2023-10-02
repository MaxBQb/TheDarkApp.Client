package lab.maxb.dark.data.local.model

import androidx.paging.PagingData
import lab.maxb.dark.domain.model.LayerSpecific
import lab.maxb.dark.domain.model.Pageable

fun <T : Any> PagingData<T>.toDomain() = LayerSpecific<Pageable<T>>(this)
