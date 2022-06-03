package lab.maxb.dark.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import lab.maxb.dark.databinding.InputListItemBinding
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder


class InputListAdapter :
    ListAdapter<ItemHolder<String>, InputListAdapter.ViewHolder>(stringHolderDiffCallback) {
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
                if (holder.absoluteAdapterPosition != NO_POSITION)
                    onItemTextChangedListener?.invoke(
                        this,
                        holder.absoluteAdapterPosition,
                        text?.toString()
                    )
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (holder.absoluteAdapterPosition != NO_POSITION)
                    onItemFocusedListener?.invoke(
                        this,
                        holder.absoluteAdapterPosition,
                        hasFocus
                    )
            }
        }
    }

    inner class ViewHolder(var binding: InputListItemBinding):
        RecyclerView.ViewHolder(binding.root)
}
