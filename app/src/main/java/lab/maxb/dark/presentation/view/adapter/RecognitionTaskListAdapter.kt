package lab.maxb.dark.presentation.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import lab.maxb.dark.databinding.RecognitionTaskListElementBinding
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.toBitmap
import lab.maxb.dark.presentation.view.adapter.RecognitionTaskListAdapter.TaskViewHolder

class RecognitionTaskListAdapter:
    PagingDataAdapter<RecognitionTask, TaskViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<RecognitionTask>() {
            override fun areItemsTheSame(oldItem: RecognitionTask, newItem: RecognitionTask): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: RecognitionTask, newItem: RecognitionTask): Boolean =
                oldItem.id == newItem.id &&
                oldItem.owner?.id == newItem.owner?.id &&
                oldItem.owner?.name == newItem.owner?.name &&
                oldItem.images?.firstOrNull()?.id == newItem.images?.firstOrNull()?.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        = RecognitionTaskListElementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).run {
            TaskViewHolder(this)
        }

    var onElementClickListener: ((view: View, item: RecognitionTask) -> Unit)?
        = null

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position) ?: return
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

    inner class TaskViewHolder(var binding: RecognitionTaskListElementBinding):
        RecyclerView.ViewHolder(binding.root)
}