package lab.maxb.dark.presentation.viewModel.utils

import java.util.*

data class ItemHolder<T>(var value: T) {
    val id = UUID.randomUUID()
}