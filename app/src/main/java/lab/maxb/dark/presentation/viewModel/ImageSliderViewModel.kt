package lab.maxb.dark.presentation.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import lab.maxb.dark.presentation.extra.takePersistablePermission


class ImageSliderViewModel(application: Application) : AndroidViewModel(application) {
    private var imageUris: MutableList<Uri> = mutableListOf()
    private val _images: MutableLiveData<List<Uri>> = MutableLiveData(listOf())
    val images = _images as LiveData<List<Uri>>
    var maxAmount = -1
    var isEditable = false

    fun setImages(uris: List<String>) {
        imageUris = uris.mapNotNull{Uri.parse(it)}.toMutableList()
        _images.value = imageUris
    }

    fun deleteImage(position: Int) {
        imageUris.removeAt(position)
        _images.value = imageUris
    }

    fun addImages(uris: List<Uri>) {
        uris.forEach {
            if (maxAmount in 1..imageUris.size)
                return@forEach
            it.takePersistablePermission(getApplication())
            imageUris.add(it)
        }
        _images.value = imageUris
    }

    fun updateImage(position: Int, uri: Uri) {
        uri.takePersistablePermission(getApplication())
        imageUris[position] = uri
        _images.value = imageUris
    }
}