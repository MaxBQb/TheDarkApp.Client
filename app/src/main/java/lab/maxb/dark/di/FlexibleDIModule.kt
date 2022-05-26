package lab.maxb.dark.di

import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.network.dark.DarkServiceImpl
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val flexibleDIModule = module {
    single { LocalDatabase.build(androidApplication()) }
    single<DarkService> { DarkServiceImpl(get()) }
}