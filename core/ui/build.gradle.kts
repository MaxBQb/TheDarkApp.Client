@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        implementation(projects.core.domain)

        defaultDependencies(false)
        images()

        // Paging
        implementation(libs.paging.runtime)
        api(projects.feature.paging.domain)

        // JC Navigation
        implementation(libs.compose.destinations.core)

        // Compose
        implementation(libs.compose.theme.adapter)
        implementation(libs.compose.material)
    }
}