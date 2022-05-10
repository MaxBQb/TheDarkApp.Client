package lab.maxb.dark.presentation.viewModel.utils

import java.util.*

data class ItemHolder<T>(
    var value: T,
    val id: UUID = UUID.randomUUID(),
)

fun <T> T.asItemHolder()
    = ItemHolder(this)

fun <T, R> ItemHolder<T>.map(value: R)
    = ItemHolder(value, id)

inline fun <T, R> ItemHolder<T>.map(block: (T) -> R)
    = map(block(value))