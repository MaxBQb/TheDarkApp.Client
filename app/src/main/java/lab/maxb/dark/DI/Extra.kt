package lab.maxb.dark.DI

import lab.maxb.dark.Presentation.Extra.SessionHolder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val MODULE_extra = module {
    single { SessionHolder(androidContext()) }
}