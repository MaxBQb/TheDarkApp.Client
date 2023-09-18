package lab.maxb.dark.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lab.maxb.dark.data.datasource.local.ProfileLocalDataSource
import lab.maxb.dark.data.datasource.local.clear
import lab.maxb.dark.data.datasource.local.save
import lab.maxb.dark.data.datasource.remote.AuthRemoteDataSource
import lab.maxb.dark.data.model.remote.toDomain
import lab.maxb.dark.data.model.remote.toNetworkDTO
import lab.maxb.dark.data.utils.InMemRefreshController
import lab.maxb.dark.data.utils.ResourceImpl
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.AuthState
import lab.maxb.dark.domain.model.Mapper
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.getCastMapper
import lab.maxb.dark.domain.operations.toProfile
import lab.maxb.dark.domain.repository.ProfileRepository
import org.koin.core.annotation.Single
import java.time.Duration

@Single
class ProfileRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: ProfileLocalDataSource,
) : ProfileRepository {
    override val isTokenExpired = remoteDataSource.authState.map {
        it is AuthState.NotAuthorized
    }
    private val _credentials = MutableStateFlow<AuthCredentials?>(null)


    override val profileResource = ResourceImpl<AuthCredentials?, Profile, Profile>(
        refreshController = InMemRefreshController(Duration.ofHours(12).toMillis()),
        fetchLocal = { localDataSource.data },
        fetchRemote = remote@ {
            it ?: return@remote null
            val request = it.toNetworkDTO()
            val response = (if (it.initial)
                remoteDataSource.signup(request)
            else
                remoteDataSource.login(request)).toDomain(it)
            response.toProfile()
        },
        localMapper = Mapper.getCastMapper(),
        reversedLocalMapper = Mapper.getCastMapper(),
        localStore = localDataSource::save,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override val profile = _credentials.flatMapLatest {
        profileResource.query(it, true)
    }

    override suspend fun sendCredentials(credentials: AuthCredentials): Profile? {
        _credentials.value = credentials
        return profile.firstOrNull()
    }

    override suspend fun clear() {
        _credentials.value = null
        localDataSource.clear()
    }
}
