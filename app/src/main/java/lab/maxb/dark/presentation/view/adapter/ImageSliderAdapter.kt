package lab.maxb.dark.presentation.view.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lab.maxb.dark.databinding.ImageElementBinding
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder

typealias ComparableImage = Pair<String, Bitmap>

open class ImageSliderAdapter : ListAdapter<ItemHolder<ComparableImage>,
            ImageSliderAdapter.ImageSliderViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ImageSliderViewHolder(ImageElementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: ImageSliderAdapter.ImageSliderViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.binding.imageContent.setImageBitmap(item.value.second)
    }

    inner class ImageSliderViewHolder(var binding: ImageElementBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ItemHolder<ComparableImage>>() {
            override fun areItemsTheSame(oldItem: ItemHolder<ComparableImage>, newItem: ItemHolder<ComparableImage>)
                = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ItemHolder<ComparableImage>, newItem: ItemHolder<ComparableImage>)
                = oldItem.value.first == newItem.value.first
        }.let { AsyncDifferConfig.Builder(it).build() }
    }
}