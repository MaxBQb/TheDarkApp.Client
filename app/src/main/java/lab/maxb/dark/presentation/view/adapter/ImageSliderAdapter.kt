package lab.maxb.dark.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import lab.maxb.dark.databinding.ImageElementBinding
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder


open class ImageSliderAdapter(
    private val getImageLoader: (String) -> RequestBuilder<*>,
) : ListAdapter<ItemHolder<String>,
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
        getImageLoader(getItem(position).value).into(holder.binding.imageContent)
    }

    inner class ImageSliderViewHolder(var binding: ImageElementBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ItemHolder<String>>() {
            override fun areItemsTheSame(oldItem: ItemHolder<String>, newItem: ItemHolder<String>)
                = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ItemHolder<String>, newItem: ItemHolder<String>)
                = oldItem.value == newItem.value
        }.let { AsyncDifferConfig.Builder(it).build() }
    }
}