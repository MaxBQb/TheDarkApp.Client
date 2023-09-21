package lab.maxb.dark.ui.extra
import lab.maxb.dark.domain.operations.randomUUID

data class ItemHolder<T>(
    val value: T,
    val id: String = randomUUID,
)

fun <T, R> ItemHolder<T>.map(value: R)
    = ItemHolder(value, id)

inline fun <T, R> ItemHolder<T>.map(block: (T) -> R)
    = map(block(value))
