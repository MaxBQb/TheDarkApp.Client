package lab.maxb.dark.DI

import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val flexibleDIModule = module {
    single { LocalDatabase.build(androidApplication()) }
}