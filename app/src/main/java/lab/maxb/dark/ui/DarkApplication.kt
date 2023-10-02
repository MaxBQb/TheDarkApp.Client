package lab.maxb.dark.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import lab.maxb.dark.data.CoreDataModule
import lab.maxb.dark.data.FeatureArticlesDataModule
import lab.maxb.dark.data.FeatureAuthDataModule
import lab.maxb.dark.data.FeatureDataModule
import lab.maxb.dark.data.FeaturePagingDataModule
import lab.maxb.dark.data.FeatureSettingsDataModule
import lab.maxb.dark.data.FeatureTasksDataModule
import lab.maxb.dark.data.FeatureUsersDataModule
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
                FeatureDataModule().module,
                CoreDomainModule().module, CoreDataModule().module,
                FeatureNavigationUIModule().module,
                FeatureArticlesDataModule().module, FeatureArticlesUIModule().module,
                FeatureUsersDataModule().module, FeatureWelcomeUIModule().module,
                FeatureSettingsDataModule().module, FeatureSettingsUIModule().module,
                FeatureAuthDataModule().module, FeatureAuthUIModule().module,
                FeatureTasksDataModule().module,
                FeatureAddTaskUIModule().module,
                FeatureSolveTaskUIModule().module,
                FeatureTasksListUIModule().module,
                FeaturePagingDataModule().module,
                FeatureNavigationDrawerUIModule().module,
            )
        }
    }
}