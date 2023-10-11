@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        implementation(projects.core.domain)

        defaultDependencies(false)

        // Paging
        implementation(libs.paging.runtime)
        implementation(projects.feature.paging.domain)

        // Compose
        implementation(libs.compose.theme.adapter)
        implementation(libs.compose.material)
    }
}