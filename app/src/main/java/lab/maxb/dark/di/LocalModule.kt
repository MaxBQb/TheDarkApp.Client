package lab.maxb.dark.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lab.maxb.dark.data.local.dataStore.getAead
import lab.maxb.dark.data.local.room.LocalDatabase
import lab.maxb.dark.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Provides
    @Singleton
    fun provideLocalDb(app: Application)
        = LocalDatabase.build(app)

    @Provides
    @Singleton
    fun provideLocalStorage(app: Application): LocalStorage
        = provideLocalDb(app)

    @Provides
    @Singleton
    fun provideAead(@ApplicationContext context: Context)
        = getAead(context)
}