package lab.maxb.dark

import android.app.Application
import lab.maxb.dark.DI.MODULE_extra
import lab.maxb.dark.DI.MODULE_repository
import lab.maxb.dark.DI.MODULE_viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@Suppress("unused") // Used by AndroidManifest.xml
class DarkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DarkApplication)
            modules(
                MODULE_repository,
                MODULE_viewModels,
                MODULE_extra,
            )
        }
    }
}