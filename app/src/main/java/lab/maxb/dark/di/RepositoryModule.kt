package lab.maxb.dark.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lab.maxb.dark.data.local.dataStore.ProfileDataSource
import lab.maxb.dark.data.local.dataStore.ProfileDataSourceImpl
import lab.maxb.dark.data.remote.synonymizer.SynonymFounder
import lab.maxb.dark.data.repository.ProfileRepositoryImpl
import lab.maxb.dark.data.repository.RecognitionTasksRepositoryImpl
import lab.maxb.dark.data.repository.SettingsRepositoryImpl
import lab.maxb.dark.data.repository.UsersRepositoryImpl
import lab.maxb.dark.domain.repository.*

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindProfileRepository(repo: ProfileRepositoryImpl): ProfileRepository
    @Binds
    abstract fun bindSynonymsRepository(repo: SynonymFounder): SynonymsRepository
    @Binds
    abstract fun bindTasksRepository(repo: RecognitionTasksRepositoryImpl): RecognitionTasksRepository
    @Binds
    abstract fun bindUsersRepository(repo: UsersRepositoryImpl): UsersRepository
    @Binds
    abstract fun bindSettingsDatasource(repo: SettingsRepositoryImpl): SettingsRepository
    @Binds
    abstract fun bindProfileDatasource(repo: ProfileDataSourceImpl): ProfileDataSource
}