package lab.maxb.dark.Presentation.View.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import lab.maxb.dark.databinding.InputListItemBinding


class InputListAdapter(
    data: List<String>? = null
) : RecyclerView.Adapter<InputListAdapter.ViewHolder>() {
    private val data: List<String> = data ?: listOf()
    var onItemTextChangedListener: ((input: EditText, newValue: String?, position: Int) -> Unit)?
        = null

    var onItemFocusedListener: ((input: EditText, position: Int, hasFocus: Boolean) -> Unit)?
        = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val viewHolder = ViewHolder(
            InputListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
        viewHolder.setListeners()
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.input.setText(data[position])
    }

    inner class ViewHolder(var binding: InputListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun setListeners() {
            binding.input.doOnTextChanged { text, _, _, _ ->
                if (adapterPosition != NO_POSITION)
                    onItemTextChangedListener?.invoke(binding.input, text.toString(), adapterPosition)
            }

            binding.input.setOnFocusChangeListener { _, hasFocus ->
                if (adapterPosition != NO_POSITION)
                    onItemFocusedListener?.invoke(binding.input, adapterPosition, hasFocus)
            }
        }
    }

}