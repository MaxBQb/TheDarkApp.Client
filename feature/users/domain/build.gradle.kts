@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.jvm.library)
    alias(libs.plugins.convention.dependencies.domain)
}

domainModule {
    dependencies {
        api(projects.core.domain)
        api(projects.feature.auth.domain)

        defaultDependencies()
    }
}