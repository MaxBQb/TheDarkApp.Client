package lab.maxb.dark.presentation.repository.network.dark

import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.repository.network.dark.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody


interface DarkService {
    suspend fun getAllTasks(page: Int, size: Int): List<RecognitionTaskListViewDTO>?
    suspend fun getTask(id: String): RecognitionTaskFullViewDTO?
    suspend fun addTask(task: RecognitionTaskCreationDTO): String?
    suspend fun markTask(id: String, isAllowed: Boolean): Boolean
    suspend fun solveTask(id: String, answer: String): Boolean
    suspend fun addImage(id: String, filePart: MultipartBody.Part): String?
    suspend fun downloadImage(path: String): ResponseBody?
    suspend fun getUser(id: String): User?
    suspend fun login(request: AuthRequest): AuthResponse
    suspend fun signup(request: AuthRequest): AuthResponse
}
