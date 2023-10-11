@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.dependencies.ui)
}

uiModule {
    dependencies {
        projects {
            api(core.ui)
            implementation(core.components)
            feature {
                api(users.domain)
                api(articles.domain)
                implementation(navigationDrawer.ui)
            }
        }
        defaultDependencies()
        navigation()
    }
}