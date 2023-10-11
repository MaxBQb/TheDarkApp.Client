@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        api(projects.core.ui)
        api(projects.feature.tasks.domain)
        api(projects.feature.navigation.api)

        defaultDependencies()
    }
}