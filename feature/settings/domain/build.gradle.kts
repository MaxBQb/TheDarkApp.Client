@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.jvm.library)
    alias(libs.plugins.convention.dependencies.domain)
    id(libs.plugins.kotlin.serialization.id)
}

domainModule {
    dependencies {
        api(libs.kotlinx.serialization.json)

        defaultDependencies()
    }
}