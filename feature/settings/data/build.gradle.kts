@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.data)
    alias(libs.plugins.convention.dependencies.data)
}

dataModule {
    dependencies {
        api(projects.core.data)
        api(projects.feature.settings.domain)

        defaultDependencies()
        dataStore()
    }
}