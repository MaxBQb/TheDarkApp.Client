@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    features {
        hasPaging = true
    }
    dependencies {
        projects.core {
            implementations(domain, data, ui, components)
        }
        api(projects.feature.tasks.domain)
        implementation(projects.feature.navigationDrawer.ui)

        defaultDependencies()
        navigation()
        images()
    }
}