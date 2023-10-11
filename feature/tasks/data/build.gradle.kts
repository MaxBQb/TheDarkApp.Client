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
        projects {
            core { apis(domain, data) }
            feature {
                api(tasks.domain)
                api(auth.domain)
                implementation(users.data)
            }
        }

        defaultDependencies()
        room()
        retrofit()

        // Retrofit2 Networking essentials
        implementation(libs.retrofit.converter.gson)
    }
}