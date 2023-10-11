@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        projects.feature {
            implementation(articles.ui)
            implementation(settings.ui)
            api(welcome.ui)
            api(auth.ui)
            tasks.ui { implementations(add, solve, list) }
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