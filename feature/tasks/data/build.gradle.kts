@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.data)
    alias(libs.plugins.convention.dependencies.data)
    id(libs.plugins.secrets.id)
}

dataModule {
    features {
        hasPaging = true
    }
    dependencies {
        implementation(projects.core.domain)
        implementation(projects.core.data)
        implementation(projects.feature.tasks.domain)
        api(projects.feature.auth.domain)
        api(projects.feature.users.data)

        defaultDependencies()
        room()
        retrofit()

        // Retrofit2 Networking essentials
        implementation(libs.retrofit.converter.gson)
    }
}