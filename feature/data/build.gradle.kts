@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.data)
    alias(libs.plugins.convention.dependencies.data)
    id(libs.plugins.secrets.id)
}

dataModule {
    dependencies {
        implementation(projects.core.domain)
        implementation(projects.core.data)
        implementation(projects.feature.paging.data)
        api(projects.feature.articles.data)
        api(projects.feature.auth.data)
        api(projects.feature.tasks.data)
        api(projects.feature.users.data)

        defaultDependencies()
        room(true)
        retrofit()
        dataStore(true)

        // Retrofit2 essentials
        implementation(libs.retrofit.converter.gson)
        implementation(libs.logging.interceptor)
    }
}