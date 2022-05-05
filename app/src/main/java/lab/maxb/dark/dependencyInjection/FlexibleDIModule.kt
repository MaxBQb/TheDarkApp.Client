package lab.maxb.dark.dependencyInjection

import lab.maxb.dark.presentation.repository.room.LocalDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val flexibleDIModule = module {
    single { LocalDatabase.build(androidApplication()) }
}