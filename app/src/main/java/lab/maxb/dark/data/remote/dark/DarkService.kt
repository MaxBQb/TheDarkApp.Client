package lab.maxb.dark.data.remote.dark

import com.bumptech.glide.load.model.GlideUrl
import lab.maxb.dark.data.model.remote.*
import lab.maxb.dark.domain.model.User
import okhttp3.MultipartBody


interface DarkService {
    suspend fun getAllTasks(page: Int, size: Int): List<RecognitionTaskListViewNetworkDTO>?
    suspend fun getTask(id: String): RecognitionTaskFullViewNetworkDTO?
    suspend fun addTask(task: RecognitionTaskCreationNetworkDTO): String?
    suspend fun markTask(id: String, isAllowed: Boolean): Boolean
    suspend fun solveTask(id: String, answer: String): Boolean
    suspend fun addImage(id: String, filePart: MultipartBody.Part): String?
    fun getImageSource(path: String): GlideUrl
    suspend fun getUser(id: String): User?
    suspend fun login(request: AuthRequest): AuthResponse
    suspend fun signup(request: AuthRequest): AuthResponse

    var onAuthRequired: (suspend () -> Unit)?
}
