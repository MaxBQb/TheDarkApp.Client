@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
    id(libs.plugins.secrets.id)
}

uiModule {
    dependencies {
        api(projects.core.ui)
        implementation(projects.core.components)
        implementation(projects.feature.settings.domain)

        // JC Dialogs
        implementation(libs.material.dialogs)

        // JC Settings
        implementation(libs.compose.settings)

        defaultDependencies()
        navigation()

        // Compose
        implementation(libs.compose.material) // TODO rewrite settings without use of m1
    }
}