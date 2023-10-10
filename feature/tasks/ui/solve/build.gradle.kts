@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        projects.core {
            implementations(domain, data, ui, components)
        }
        implementation(projects.feature.tasks.domain)

        defaultDependencies()
        navigation()
    }
}