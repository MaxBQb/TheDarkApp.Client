package lab.maxb.dark.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import lab.maxb.dark.databinding.RecognitionTaskListElementBinding
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.view.adapter.RecognitionTaskListAdapter.TaskViewHolder

class RecognitionTaskListAdapter(
    private val manager: RequestManager,
    private val getImageLoader: RequestManager.(String) -> RequestBuilder<*>,
): PagingDataAdapter<RecognitionTask, TaskViewHolder>(COMPARATOR) {

    val preloader = ImagePreloader(
        manager,
        getItem = {
            getItem(it)?.images?.firstOrNull()
        },
        getImageLoader = getImageLoader
    )

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<RecognitionTask>() {
            override fun areItemsTheSame(oldItem: RecognitionTask, newItem: RecognitionTask): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: RecognitionTask, newItem: RecognitionTask): Boolean =
                oldItem.owner?.id == newItem.owner?.id &&
                oldItem.owner?.name == newItem.owner?.name &&
                oldItem.images?.firstOrNull() == newItem.images?.firstOrNull()
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

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)
        taskOwnerName.text = item?.owner?.name ?: ""
        item?.images?.firstOrNull()?.let {
            getImageLoader(manager, it).into(taskImage)
        } ?: manager.clear(taskImage)
        root.setOnClickListener { v ->
            if (position != RecyclerView.NO_POSITION && item != null)
                onElementClickListener?.invoke(v, item)
        }
    }

    inner class TaskViewHolder(var binding: RecognitionTaskListElementBinding):
        RecyclerView.ViewHolder(binding.root)
}