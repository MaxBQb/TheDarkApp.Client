package lab.maxb.dark.Presentation.View.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lab.maxb.dark.Presentation.Extra.toBitmap
import lab.maxb.dark.databinding.ImageElementBinding


open class ImageSliderAdapter(
    private var images: List<String?>,
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
        try {
            holder.binding.imageContent.setImageBitmap(
                Uri.parse(images[position]).toBitmap(
                    holder.itemView.context,
                    holder.binding.imageContent.layoutParams.width,
                    holder.binding.imageContent.layoutParams.height,
                )
            )
        } catch (e: Throwable) {
            handleCursedImage(e, holder, position)
            e.printStackTrace()
        }
    }

    protected open fun handleCursedImage(exception: Throwable,
                                         holder: ImageSliderAdapter.ImageSliderViewHolder,
                                         position: Int) {
        val imagesNew = images.toMutableList()
        imagesNew.removeAt(position)
        images = imagesNew
        notifyDataSetChanged()
    }

    override fun getItemCount() = images.size

    inner class ImageSliderViewHolder(var binding: ImageElementBinding) :
        RecyclerView.ViewHolder(binding.root)
}