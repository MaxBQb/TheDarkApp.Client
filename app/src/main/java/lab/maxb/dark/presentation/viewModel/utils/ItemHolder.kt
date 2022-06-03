package lab.maxb.dark.presentation.viewModel.utils
import lab.maxb.dark.domain.operations.randomUUID

class ItemHolder<T>(
    var value: T,
    val id: String = randomUUID,
)

fun <T, R> ItemHolder<T>.map(value: R)
    = ItemHolder(value, id)

inline fun <T, R> ItemHolder<T>.map(block: (T) -> R)
    = map(block(value))
