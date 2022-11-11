package lab.maxb.dark.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lab.maxb.dark.data.remote.dark.DarkService
import lab.maxb.dark.data.remote.dark.DarkServiceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteModule {
    @Binds
    abstract fun bindDarkService(repo: DarkServiceImpl): DarkService
}