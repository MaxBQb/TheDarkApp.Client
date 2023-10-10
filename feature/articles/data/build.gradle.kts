@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.data)
    alias(libs.plugins.convention.dependencies.data)
}

dataModule {
    features {
        hasPaging = true
    }
    dependencies {
        implementation(projects.core.domain)
        implementation(projects.core.data)
        implementation(projects.feature.articles.domain)
        api(projects.feature.users.domain)
        api(projects.feature.users.data)

        defaultDependencies()
        room()
        retrofit()
    }
}