@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.data)
    alias(libs.plugins.convention.dependencies.data)
}

dataModule {
    dependencies {
        api(projects.core.domain)
        api(projects.feature.auth.domain)

        defaultDependencies()
        room()
        dataStore()

        // Retrofit2 Networking
        implementation(libs.retrofit.core)
        implementation(libs.logging.interceptor)
    }
}