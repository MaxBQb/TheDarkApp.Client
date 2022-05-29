package lab.maxb.dark.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import lab.maxb.dark.databinding.ImageElementBinding
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder


open class ImageSliderAdapter(
    private val getImageLoader: (String) -> RequestBuilder<*>,
) : ListAdapter<ItemHolder<String>,
    ImageSliderAdapter.ImageSliderViewHolder>(stringHolderDiffCallback) {

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
}