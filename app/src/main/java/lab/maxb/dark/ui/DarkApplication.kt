package lab.maxb.dark.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import lab.maxb.dark.data.CoreDataModule
import lab.maxb.dark.domain.CoreDomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module


class DarkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@DarkApplication)
            modules(
                AppModule().module,
                CoreDomainModule().module, CoreDataModule().module,
                FeatureNavigationUIModule().module,
                FeatureArticlesUIModule().module,
                FeatureWelcomeUIModule().module,
                FeatureSettingsUIModule().module,
                FeatureAuthUIModule().module,
                FeatureAddTaskUIModule().module,
                FeatureSolveTaskUIModule().module,
                FeatureTasksListUIModule().module,
                FeatureNavigationDrawerUIModule().module,
            )
        }
    }
}