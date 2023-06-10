package lab.maxb.dark.data.remote.dark

import lab.maxb.dark.data.model.remote.ArticleCreationNetworkDTO
import lab.maxb.dark.data.model.remote.ArticleNetworkDTO
import lab.maxb.dark.data.model.remote.AuthRequest
import lab.maxb.dark.data.model.remote.AuthResponse
import lab.maxb.dark.data.model.remote.RecognitionTaskCreationNetworkDTO
import lab.maxb.dark.data.model.remote.RecognitionTaskFullViewNetworkDTO
import lab.maxb.dark.data.model.remote.RecognitionTaskListViewNetworkDTO
import lab.maxb.dark.domain.model.User
import okhttp3.MultipartBody


interface DarkService {
    suspend fun getAllTasks(page: Int, size: Int): List<RecognitionTaskListViewNetworkDTO>?
    suspend fun getTask(id: String): RecognitionTaskFullViewNetworkDTO?
    suspend fun addTask(task: RecognitionTaskCreationNetworkDTO): RecognitionTaskFullViewNetworkDTO?
    suspend fun markTask(id: String, isAllowed: Boolean): Boolean
    suspend fun solveTask(id: String, answer: String): Boolean
    suspend fun addImage(filePart: MultipartBody.Part): String?
    fun getImageSource(path: String): String
    suspend fun getAllArticles(page: Int, size: Int): List<ArticleNetworkDTO>?
    suspend fun updateArticle(id: String, article: ArticleCreationNetworkDTO): ArticleNetworkDTO?
    suspend fun addArticle(article: ArticleCreationNetworkDTO): ArticleNetworkDTO?
    suspend fun getUser(id: String): User?
    suspend fun login(request: AuthRequest): AuthResponse
    suspend fun signup(request: AuthRequest): AuthResponse

    var onAuthRequired: (suspend () -> Unit)?
}
