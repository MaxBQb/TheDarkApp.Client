package lab.maxb.dark

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import lab.maxb.dark.di.StaticDIModule
import lab.maxb.dark.di.flexibleDIModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.*


@Suppress("unused") // Used by AndroidManifest.xml
class DarkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@DarkApplication)
            modules(StaticDIModule().module, flexibleDIModule)
        }
    }
}