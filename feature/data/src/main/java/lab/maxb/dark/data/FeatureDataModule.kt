package lab.maxb.dark.data

import androidx.room.RoomDatabase
import lab.maxb.dark.data.local.LocalDatabase
import lab.maxb.dark.data.local.getAead
import lab.maxb.dark.data.remote.interceptor.LoggingInterceptor
import lab.maxb.dark.data.remote.interceptor.LoggingInterceptorImpl
import lab.maxb.dark.domain.repository.LocalStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

@Module([FeatureDataModuleManual::class])
@ComponentScan("lab.maxb.dark")
class FeatureDataModule

internal class FeatureDataModuleManual {
    internal val module = module {
        single { LocalDatabase.build(androidApplication()) }
        single<LocalStorage> { get<LocalDatabase>() }
        single<RoomDatabase> { get<LocalDatabase>() }
        factory { get<LocalDatabase>().articles() }
        factory { get<LocalDatabase>().recognitionTasks() }
        factory { get<LocalDatabase>().remoteKeys() }
        factory { get<LocalDatabase>().users() }
        factory<LoggingInterceptor> { LoggingInterceptorImpl() }
        single { getAead(androidApplication()) }
    }
}