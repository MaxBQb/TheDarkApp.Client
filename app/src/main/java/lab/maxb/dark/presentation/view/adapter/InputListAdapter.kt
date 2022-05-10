package lab.maxb.dark.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import lab.maxb.dark.databinding.InputListItemBinding
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder


class InputListAdapter : ListAdapter<ItemHolder<String>, InputListAdapter.ViewHolder>(
    AsyncDifferConfig.Builder(DiffCallback).build()
) {
    var onItemTextChangedListener: ((input: EditText, position: Int, newValue: String?) -> Unit)?
        = null

    var onItemFocusedListener: ((input: EditText, position: Int, hasFocus: Boolean) -> Unit)?
        = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int) = ViewHolder(
        InputListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with (holder.binding.input){
            setText(item.value)
            doOnTextChanged { text, _, _, _ ->
                if (holder.adapterPosition != NO_POSITION)
                    onItemTextChangedListener?.invoke(this, holder.adapterPosition, text?.toString())
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (holder.adapterPosition != NO_POSITION)
                    onItemFocusedListener?.invoke(this, holder.adapterPosition, hasFocus)
            }
        }
    }

    inner class ViewHolder(var binding: InputListItemBinding):
        RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<ItemHolder<String>>() {
        override fun areItemsTheSame(oldItem: ItemHolder<String>, newItem: ItemHolder<String>) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ItemHolder<String>, newItem: ItemHolder<String>) =
            oldItem.value == newItem.value
    }
}
