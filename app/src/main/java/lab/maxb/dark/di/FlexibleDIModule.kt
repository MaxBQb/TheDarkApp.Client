package lab.maxb.dark.di

import lab.maxb.dark.data.local.dataStore.getAead
import lab.maxb.dark.data.local.room.LocalDatabase
import lab.maxb.dark.data.remote.dark.DarkService
import lab.maxb.dark.data.remote.dark.DarkServiceImpl
import lab.maxb.dark.domain.repository.LocalStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val flexibleDIModule = module {
    single { LocalDatabase.build(androidApplication()) }
    single<LocalStorage> { get<LocalDatabase>() }
    single<DarkService> { DarkServiceImpl(get()) }
    single { getAead(androidApplication()) }
}