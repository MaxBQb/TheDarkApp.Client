@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        implementation(projects.core.ui)
        implementation(projects.core.domain)
        implementation(projects.feature.tasks.domain)
        api(projects.feature.navigation.api)

        defaultDependencies()
    }
}