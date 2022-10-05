package lab.maxb.dark.di

import lab.maxb.dark.data.local.room.LocalDatabase
import lab.maxb.dark.data.remote.dark.DarkService
import lab.maxb.dark.data.remote.dark.DarkServiceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val flexibleDIModule = module {
    single { LocalDatabase.build(androidApplication()) }
    single<DarkService> { DarkServiceImpl(get()) }
}