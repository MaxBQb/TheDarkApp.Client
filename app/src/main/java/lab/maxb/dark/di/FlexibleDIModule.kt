package lab.maxb.dark.di

import lab.maxb.dark.data.local.dataStore.getAead
import lab.maxb.dark.data.local.room.LocalDatabase
import lab.maxb.dark.data.remote.dark.DarkServiceAPI
import lab.maxb.dark.data.remote.dark.RetrofitDarkClient
import lab.maxb.dark.data.remote.dark.routes.ArticlesAPI
import lab.maxb.dark.data.remote.dark.routes.AuthAPI
import lab.maxb.dark.data.remote.dark.routes.ImagesAPI
import lab.maxb.dark.data.remote.dark.routes.RecognitionTasksAPI
import lab.maxb.dark.data.remote.dark.routes.UsersAPI
import lab.maxb.dark.domain.repository.LocalStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.binds
import org.koin.dsl.module

internal val flexibleDIModule = module {
    single { LocalDatabase.build(androidApplication()) }
    single<LocalStorage> { get<LocalDatabase>() }
    factory { get<LocalDatabase>().articles() }
    factory { get<LocalDatabase>().recognitionTasks() }
    factory { get<LocalDatabase>().remoteKeys() }
    factory { get<LocalDatabase>().users() }
    single { RetrofitDarkClient.buildDarkService() } binds arrayOf(
        DarkServiceAPI::class,
        ArticlesAPI::class,
        RecognitionTasksAPI::class,
        ImagesAPI::class,
        UsersAPI::class,
        AuthAPI::class,
    )
    single { getAead(androidApplication()) }
}