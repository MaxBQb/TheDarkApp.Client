package lab.maxb.dark

import android.app.Application
import lab.maxb.dark.DI.StaticDIModule
import lab.maxb.dark.DI.flexibleDIModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

@Suppress("unused") // Used by AndroidManifest.xml
class DarkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@DarkApplication)
            modules(StaticDIModule().module, flexibleDIModule)
        }
    }
}