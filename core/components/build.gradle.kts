@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.android.locales)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        implementation(projects.core.data)
        implementation(projects.core.ui)
        implementation(projects.feature.navigationDrawer.ui)

        defaultDependencies(false)
        navigation(false)
        paging()
        images()

        // JC Dialogs
        implementation(libs.material.dialogs)

        // JC Accompanist
        implementation(libs.accompanist.pager.indicators)
        implementation(libs.accompanist.adaptive)
    }
}