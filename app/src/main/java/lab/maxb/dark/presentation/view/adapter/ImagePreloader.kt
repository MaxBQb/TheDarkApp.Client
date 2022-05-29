package lab.maxb.dark.presentation.view.adapter

import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import java.util.*

class ImagePreloader(
    private val manager: RequestManager,
    private val getItem: (Int) -> String?,
    private val getImageLoader: RequestManager.(String) -> RequestBuilder<*>,
    maxPreload: Int = 10,
) {
    private val sizeProvider = ViewPreloadSizeProvider<String>()
    private val modelProvider = MyPreloadModelProvider()
    val raw = RecyclerViewPreloader(
        manager,
        modelProvider,
        sizeProvider,
        maxPreload,
    )

    private inner class MyPreloadModelProvider : ListPreloader.PreloadModelProvider<String> {
        override fun getPreloadItems(position: Int): MutableList<String> {
            val url = getItem(position)
            return if (url.isNullOrEmpty()) {
                Collections.emptyList()
            } else Collections.singletonList(url)
        }

        override fun getPreloadRequestBuilder(item: String): RequestBuilder<*>
            = getImageLoader(manager, item)
    }
}