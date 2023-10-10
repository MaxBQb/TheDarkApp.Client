package lab.maxb.dark.ui

import lab.maxb.dark.data.CoreDataModule
import lab.maxb.dark.data.FeatureArticlesDataModule
import lab.maxb.dark.data.FeatureAuthDataModule
import lab.maxb.dark.data.FeatureDataModule
import lab.maxb.dark.data.FeaturePagingDataModule
import lab.maxb.dark.data.FeatureSettingsDataModule
import lab.maxb.dark.data.FeatureTasksDataModule
import lab.maxb.dark.data.FeatureUsersDataModule
import lab.maxb.dark.domain.FeatureArticlesDomainModule
import lab.maxb.dark.domain.FeatureAuthDomainModule
import lab.maxb.dark.domain.FeatureSettingsDomainModule
import lab.maxb.dark.domain.FeatureTasksDomainModule
import lab.maxb.dark.domain.FeatureUsersDomainModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.*
import org.koin.core.annotation.Module as AModule

@AModule
@ComponentScan("lab.maxb.dark")
class AppModule

internal val allModules get() = module {
    includesFeatures()
    includes(
        AppModule().module,
        CoreDataModule().module,
    )
}

private fun Module.includesFeatures() {
    includesArticles()
    includesUsers()
    includesSettings()
    includesAuth()
    includesTasks()
    includes(
        FeatureDataModule().module,
        FeatureNavigationUIModule().module,
        FeaturePagingDataModule().module,
        FeatureNavigationDrawerUIModule().module,
    )
}

private fun Module.includesArticles() = includes(
    FeatureArticlesDomainModule().module,
    FeatureArticlesDataModule().module,
    FeatureArticlesUIModule().module,
)

private fun Module.includesUsers() = includes(
    FeatureUsersDomainModule().module,
    FeatureUsersDataModule().module,
    FeatureWelcomeUIModule().module,
)

private fun Module.includesSettings() = includes(
    FeatureSettingsDomainModule().module,
    FeatureSettingsDataModule().module,
    FeatureSettingsUIModule().module,
)

private fun Module.includesAuth() = includes(
    FeatureAuthDomainModule().module,
    FeatureAuthDataModule().module,
    FeatureAuthUIModule().module,
)
private fun Module.includesTasks() {
    includesTasksUI()
    includes(
        FeatureTasksDomainModule().module,
        FeatureTasksDataModule().module,
    )
}
private fun Module.includesTasksUI() = includes(
    FeatureAddTaskUIModule().module,
    FeatureSolveTaskUIModule().module,
    FeatureTasksListUIModule().module,
)
