@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        api(projects.core.ui)
        implementation(projects.core.components)
        api(projects.feature.settings.domain)
        api(projects.feature.auth.domain)

        // JC Dialogs
        implementation(libs.material.dialogs)

        defaultDependencies()
        navigation()
    }
}