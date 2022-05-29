package lab.maxb.dark.presentation.viewModel.utils
import java.util.*

class ItemHolder<T>(
    var value: T,
    val id: UUID = UUID.randomUUID(),
)

fun <T, R> ItemHolder<T>.map(value: R)
    = ItemHolder(value, id)

inline fun <T, R> ItemHolder<T>.map(block: (T) -> R)
    = map(block(value))
