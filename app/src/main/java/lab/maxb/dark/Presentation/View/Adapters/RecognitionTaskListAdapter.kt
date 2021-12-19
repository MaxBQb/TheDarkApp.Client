package lab.maxb.dark.Presentation.View.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Extra.toBitmap
import lab.maxb.dark.Presentation.View.Adapters.RecognitionTaskListAdapter.RecognitionTaskViewHolder
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
                Uri.parse(item.images?.get(0)).toBitmap(
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