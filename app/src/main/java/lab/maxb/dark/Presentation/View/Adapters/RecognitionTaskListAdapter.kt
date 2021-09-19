package lab.maxb.dark.Presentation.View.Adapters

import lab.maxb.dark.Domain.Model.RecognitionTask
import androidx.recyclerview.widget.RecyclerView
import lab.maxb.dark.Presentation.View.Adapters.RecognitionTaskListAdapter.RecognitionTaskViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
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

    var onElementClickListener: (view: View, item: RecognitionTask) -> Unit
            = { view: View, item: RecognitionTask -> }


    override fun onBindViewHolder(holder: RecognitionTaskViewHolder, position: Int) {
        val item = data[position]
        holder.binding.taskOwnerName.text = item.owner?.name
        holder.binding.taskImage.setImageBitmap(item.image)
        holder.itemView.setOnClickListener { v ->
            onElementClickListener(v, item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecognitionTaskViewHolder(var binding: RecognitionTaskListElementBinding):
        RecyclerView.ViewHolder(binding.root)
}