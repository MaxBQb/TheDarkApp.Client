package lab.maxb.dark.Presentation.View.Adapters

import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import lab.maxb.dark.Presentation.Extra.takePersistablePermission


class EditableImageSliderAdapter(
    private var images: MutableList<String?>,
    activity: AppCompatActivity,
    private val maxAmount: Int = -1,
) : ImageSliderAdapter(images) {
    override fun onBindViewHolder(
        holder: ImageSliderAdapter.ImageSliderViewHolder,
        position: Int
    ) {
        if (images[position] == null) {
            if (maxAmount in 1..position)
                holder.binding.addImageButton.isEnabled = false
            else
                holder.binding.addImageButton.setOnClickListener { v: View ->
                if (position != RecyclerView.NO_POSITION)
                    getContent.launch(arrayOf("image/*"))
            }
            holder.binding.imageContent.visibility = View.GONE
            holder.binding.addImageButton.visibility = View.VISIBLE
        } else {
            holder.binding.addImageButton.visibility = View.GONE
            holder.binding.imageContent.visibility = View.VISIBLE
            super.onBindViewHolder(holder, position)
        }
    }

    override fun handleCursedImage(exception: Throwable,
                                   holder: ImageSliderAdapter.ImageSliderViewHolder,
                                   position: Int) {
        images.removeAt(position)
        notifyItemRemoved(position)
    }

    private val getContent = activity.activityResultRegistry.register(ADD_URI, ActivityResultContracts.OpenMultipleDocuments()) {
        uris: List<Uri?>? ->
        uris?.filterNotNull()?.forEach {
            if (maxAmount in 1 until images.size)
                return@forEach
            it.takePersistablePermission(activity)
            images.add(images.size - 1, it.toString())
        }?.also {
            //notifyItemInserted(images.size - 2) // will swipe to the end
            notifyDataSetChanged() // on current position
        }
    }

    init {
        images.add(null)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        getContent.unregister()
    }

    companion object {
        const val ADD_URI = "ImageSliderAdapter.AddUri"
    }
}