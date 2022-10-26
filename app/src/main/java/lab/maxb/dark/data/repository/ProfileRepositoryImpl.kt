package lab.maxb.dark.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lab.maxb.dark.data.local.dataStore.ProfileDataSource
import lab.maxb.dark.data.model.remote.toDomain
import lab.maxb.dark.data.model.remote.toNetworkDTO
import lab.maxb.dark.data.remote.dark.DarkService
import lab.maxb.dark.data.utils.InMemRefreshController
import lab.maxb.dark.data.utils.ResourceImpl
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.operations.toProfile
import lab.maxb.dark.domain.repository.ProfileRepository
import org.koin.core.annotation.Single
import java.time.Duration

@Single
class ProfileRepositoryImpl(
    private val networkDataSource: DarkService,
    private val localDataSource: ProfileDataSource,
) : ProfileRepository {
    private val _isTokenExpired = MutableStateFlow(false)
    private val _credentials = MutableStateFlow<AuthCredentials?>(null)
    override val isTokenExpired = _isTokenExpired.asStateFlow()

    init {
        networkDataSource.onAuthRequired = {
            _isTokenExpired.value = true
        }
    }

    override val profileResource = ResourceImpl<AuthCredentials?, Profile, Profile>(
        refreshController = InMemRefreshController(Duration.ofHours(12).toMillis()),
        fetchLocal = { localDataSource.data },
        fetchRemote = remote@ {
            it ?: return@remote null
            val request = it.toNetworkDTO()
            val response = (if (it.initial)
                networkDataSource.signup(request)
            else
                networkDataSource.login(request)).toDomain(it)
            response.toProfile()
        },
        localStore = { value -> localDataSource.updateData { value } },
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override val profile = _credentials.flatMapLatest {
        profileResource.query(it, true)
    }

    override suspend fun sendCredentials(credentials: AuthCredentials): Profile? {
        _credentials.value = credentials
        return profile.firstOrNull()
    }

    override suspend fun clear() = localDataSource.clear()
}
