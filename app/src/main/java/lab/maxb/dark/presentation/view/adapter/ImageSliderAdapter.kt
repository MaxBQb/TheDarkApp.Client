package lab.maxb.dark.presentation.view.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lab.maxb.dark.databinding.ImageElementBinding


open class ImageSliderAdapter(
    private var images: List<Bitmap>,
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
        holder.binding.imageContent.setImageBitmap(images[position])
    }

    override fun getItemCount() = images.size

    inner class ImageSliderViewHolder(var binding: ImageElementBinding) :
        RecyclerView.ViewHolder(binding.root)
}