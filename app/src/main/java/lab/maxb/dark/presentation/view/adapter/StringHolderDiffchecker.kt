package lab.maxb.dark.presentation.view.adapter

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder

val stringHolderDiffCallback = object : DiffUtil.ItemCallback<ItemHolder<String>>() {
    override fun areItemsTheSame(oldItem: ItemHolder<String>, newItem: ItemHolder<String>)
        = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ItemHolder<String>, newItem: ItemHolder<String>)
        = oldItem.value == newItem.value
}.let { AsyncDifferConfig.Builder(it).build() }
