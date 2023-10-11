@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        projects.core {
            api(ui)
            implementations(data, components)
        }
        api(projects.feature.tasks.domain)

        defaultDependencies()
        navigation()

        // JC Accompanist
        implementation(libs.accompanist.adaptive)
    }
}