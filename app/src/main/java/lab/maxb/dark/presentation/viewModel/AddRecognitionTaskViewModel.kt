package lab.maxb.dark.presentation.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.operations.createRecognitionTask
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.SynonymsRepository
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder
import lab.maxb.dark.presentation.viewModel.utils.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AddRecognitionTaskViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    profileRepository: ProfileRepository,
    private val synonymsRepository: SynonymsRepository,
) : ViewModel() {
    var imageUris: List<String> = mutableListOf()
    private val _namesRaw = mutableListOf(ItemHolder(""))
    private val _names = MutableStateFlow(_namesRaw.toList())
    val names = _names.asStateFlow()
    private val profile = profileRepository.profileState

    suspend fun addRecognitionTask() = try {
        val user = profile.first()!!.user!!
        val task = createRecognitionTask(
            _namesRaw.map{ it.value }.filter { it.isNotBlank() },
            imageUris,
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
        _names.value = _namesRaw.toList()
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
        }) _names.value = _namesRaw.toList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val suggestions = _names.mapLatest {
        synonymsRepository.getSynonyms(_namesRaw.map { it.value } .toSet())
            .map { it.trim() }
            .filterNot { it.isEmpty() }
            .toSet()
    }.stateIn(setOf())
}

