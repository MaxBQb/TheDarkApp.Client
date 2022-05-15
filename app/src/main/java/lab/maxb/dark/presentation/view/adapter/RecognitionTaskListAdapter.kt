package lab.maxb.dark.presentation.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.toBitmap
import lab.maxb.dark.presentation.view.adapter.RecognitionTaskListAdapter.RecognitionTaskViewHolder
import lab.maxb.dark.databinding.RecognitionTaskListElementBinding

class RecognitionTaskListAdapter(data: List<RecognitionTask>?) :
      RecyclerView.Adapter<RecognitionTaskViewHolder>() {
    private val data: List<RecognitionTask> = data ?: listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecognitionTaskViewHolder {
        val binding = RecognitionTaskListElementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecognitionTaskViewHolder(binding)
    }

    var onElementClickListener: ((view: View, item: RecognitionTask) -> Unit)?
        = null

    override fun onBindViewHolder(holder: RecognitionTaskViewHolder, position: Int) {
        val item = data[position]
        holder.binding.taskOwnerName.text = item.owner?.name
        try {
            holder.binding.taskImage.setImageBitmap(
                Uri.parse(item.images?.get(0)?.path).toBitmap(
                    holder.itemView.context,
                    holder.binding.taskImage.layoutParams.width,
                    holder.binding.taskImage.layoutParams.height,
                )
            )
        } catch (ignored: Throwable) {}
        holder.itemView.setOnClickListener { v ->
            if (position != RecyclerView.NO_POSITION)
                onElementClickListener?.invoke(v, item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecognitionTaskViewHolder(var binding: RecognitionTaskListElementBinding):
        RecyclerView.ViewHolder(binding.root)
}