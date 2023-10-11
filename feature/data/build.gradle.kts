@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.data)
    alias(libs.plugins.convention.dependencies.data)
    id(libs.plugins.secrets.id)
}

dataModule {
    dependencies {
        projects.core { apis(domain, data) }
        projects.feature {
            apis(
                paging.data,
                articles.data,
                auth.data,
                tasks.data,
                users.data,
            )
        }

        defaultDependencies()
        room(true)
        retrofit()
        dataStore(true)

        // Retrofit2 essentials
        implementation(libs.retrofit.converter.gson)
        implementation(libs.logging.interceptor)
    }
}