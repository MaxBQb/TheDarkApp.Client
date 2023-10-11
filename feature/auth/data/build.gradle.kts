@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.data)
    alias(libs.plugins.convention.dependencies.data)
}

dataModule {
    dependencies {
        projects.core { apis(domain, data) }
        api(projects.feature.auth.domain)

        defaultDependencies()
        retrofit()
        dataStore(true)
    }
}