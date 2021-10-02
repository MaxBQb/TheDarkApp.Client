package lab.maxb.dark.Presentation.View.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import lab.maxb.dark.Presentation.Extra.takePersistablePermission
import lab.maxb.dark.Presentation.Extra.toBitmap
import lab.maxb.dark.databinding.ImageElementBinding


class ImageSliderAdapter(
    private var images: MutableList<String?>,
    val editable: Boolean,
    activity: AppCompatActivity
) : RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageSliderAdapter.ImageSliderViewHolder {
        return ImageSliderViewHolder(
            ImageElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ImageSliderAdapter.ImageSliderViewHolder,
        position: Int
    ) {
        if (images[position] == null) {
            holder.binding.imageContent.visibility = View.GONE
            holder.binding.addImageButton.visibility = View.VISIBLE
            holder.binding.addImageButton.setOnClickListener { v: View ->
                if (position != RecyclerView.NO_POSITION)
                    getContent.launch(arrayOf("image/*"))
            }
        } else {
            holder.binding.addImageButton.visibility = View.GONE
            holder.binding.imageContent.visibility = View.VISIBLE
            try {
                holder.binding.imageContent.setImageBitmap(
                    Uri.parse(images[position]).toBitmap(
                        holder.itemView.context,
                        holder.binding.imageContent.layoutParams.width,
                        holder.binding.imageContent.layoutParams.height,
                    )
                )
            } catch (e: Throwable) {
                if (editable) {
                    images.removeAt(position)
                    notifyItemRemoved(position)
                }
                e.printStackTrace()
            }
        }
    }

    private var getContent = activity.activityResultRegistry.register(ADD_URI, ActivityResultContracts.OpenDocument()) {
        uri: Uri? ->
        uri?.let {
            it.takePersistablePermission(activity)
            images.add(images.size - 1, it.toString())
            //notifyItemInserted(images.size - 2) // will swipe to the end
            notifyDataSetChanged() // on current position
        }
    }

    override fun getItemCount() = images.size

    inner class ImageSliderViewHolder(var binding: ImageElementBinding) :
        RecyclerView.ViewHolder(binding.root)

    init {
        if (editable) {
            images.add(null)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        getContent.unregister()
    }

    companion object {
        const val ADD_URI = "ImageSliderAdapter.AddUri"
    }
}