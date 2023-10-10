@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        implementation(projects.core.ui)
        projects.feature {
            api(articles.ui)
            api(welcome.ui)
            api(settings.ui)
            api(auth.ui)
            tasks { apis(ui.add, ui.solve, ui.list) }
            api(navigationDrawer.ui)
        }

        // Coroutines
        implementation(libs.coroutines.core)

        koin()
        navigation(false)
        androidX()
        tests()

        // Compose
        implementation(platform(libs.compose.bom))
        implementation(libs.compose.runtime)
        implementation(libs.compose.ui.core)
    }
}