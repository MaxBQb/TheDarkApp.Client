@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.data)
    alias(libs.plugins.convention.dependencies.data)
}

dataModule {
    dependencies {
        implementation(projects.core.domain)
        implementation(projects.core.data)
        implementation(projects.feature.paging.domain)

        defaultDependencies()
        room(true)
    }
}