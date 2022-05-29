package lab.maxb.dark.presentation.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.operations.createRecognitionTask
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.takePersistablePermission
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.SynonymsRepository
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder
import lab.maxb.dark.presentation.viewModel.utils.firstNotNull
import lab.maxb.dark.presentation.viewModel.utils.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AddRecognitionTaskViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    profileRepository: ProfileRepository,
    private val synonymsRepository: SynonymsRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _namesRaw = mutableListOf(ItemHolder(""))
    private val _names = MutableStateFlow(_namesRaw.toList())
    val names = _names.asStateFlow()
    private var _imagesRaw = mutableListOf<ItemHolder<String>>()
    private val _images = MutableStateFlow(_imagesRaw.toList())
    val images = _images.asStateFlow()

    private val profile = profileRepository.profileState

    val allowImageAddition get() = RecognitionTask.MAX_IMAGES_COUNT > _imagesRaw.size

    fun clear() = launch {
        _namesRaw.clear()
        _namesRaw.add(ItemHolder(""))
        _imagesRaw.clear()
        updateNames()
        updateImages()
    }

    suspend fun addRecognitionTask() = try {
        val user = profile.firstNotNull().user!!
        val task = createRecognitionTask(
            _namesRaw.map { it.value }.filter { it.isNotBlank() },
            _imagesRaw.map { it.value },
            user)!!
        recognitionTasksRepository.addRecognitionTask(task)
        true
    } catch (e: Throwable) {
        e.printStackTrace()
        false
    }

    fun setText(position: Int, text: String) {
        if (position >= _namesRaw.size)
            return
        _namesRaw[position].value = text
        if (position == _namesRaw.lastIndex)
            tryExtendInputs()
        updateNames()
    }

    private fun tryExtendInputs() {
        if (_namesRaw.last().value.isNotBlank())
            _namesRaw.add(ItemHolder(""))
    }

    fun shrinkInputs() {
        var checksCount = 0
        val lastCheck = _namesRaw.lastIndex
        if (_namesRaw.removeIf {
            checksCount++ != lastCheck &&
            it.value.isBlank()
        }) updateNames()
    }

    private fun updateNames() {
        _names.value = _namesRaw.toList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val suggestions = _names.mapLatest {
        synonymsRepository.getSynonyms(_namesRaw.map { it.value } .toSet())
            .map { it.trim() }
            .filterNot { it.isEmpty() }
            .toSet()
    }.stateIn(setOf())

    fun deleteImage(position: Int) {
        _imagesRaw.removeAt(position)
        updateImages()
    }

    private fun updateImages() {
        _images.value = _imagesRaw.toList()
    }

    fun addImages(uris: List<Uri>) {
        uris.forEach {
            if (!allowImageAddition)
                return@forEach
            it.takePersistablePermission(getApplication())
            _imagesRaw.add(ItemHolder(it.toString()))
        }
        updateImages()
    }

    fun updateImage(position: Int, uri: Uri) {
        uri.takePersistablePermission(getApplication())
        _imagesRaw[position].value = uri.toString()
        updateImages()
    }
}

