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
        projects {
            core { apis(domain, data) }
            feature {
                api(articles.domain)
                api(users.domain)
                implementation(users.data)
            }
        }
        defaultDependencies()
        room()
        retrofit()
    }
}