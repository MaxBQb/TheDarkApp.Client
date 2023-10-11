@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.application)
    alias(libs.plugins.convention.dependencies.ui)
}

android {
    applicationVariants.all {
        outputs.all {
            this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            outputFileName = "DarkApp.apk"
        }
    }
}

uiModule {
    dependencies {
        projects {
            core { implementations(data, ui) }
            feature {
                auth { implementations(data, ui) }
                articles { implementations(domain, data, ui) }
                tasks {
                    implementations(domain, data)
                    ui { implementations(add, solve, list) }
                }
                settings { implementations(domain, data, ui) }
                users { implementations(domain, data) }
                implementation(welcome.ui)
                implementation(paging.data)
                implementation(navigation.impl)
                implementation(navigationDrawer.ui)
                implementation(data)
            }
        }

        defaultDependencies()
        navigation(false)

        // Compose
        implementation(libs.compose.theme.adapter)
        implementation(libs.androidx.activity.compose)
    }
}