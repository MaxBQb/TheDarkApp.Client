import org.gradle.accessors.dm.LibrariesForLibs.ConventionPluginAccessors

plugins {
    `kotlin-dsl`
}

group = "lab.maxb.dark.plugin"

dependencies {
    implementation(gradleApi())
    implementation(libs.gradlePlugin.core)
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.ksp)

    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

inline fun NamedDomainObjectContainer<PluginDeclaration>.registerByClass(
    className: String, crossinline block: (ConventionPluginAccessors) -> Provider<PluginDependency>,
) {
    val pluginId = block(libs.plugins.convention).get().pluginId
    val pluginParts = pluginId.split(".")
    val pluginName = pluginParts.last()
    val pluginPath = pluginParts.dropLast(1)

    register(pluginName) {
        id = pluginId
        implementationClass = (pluginPath + className).joinToString(".")
    }
}

gradlePlugin {
    plugins {
        registerByClass("AndroidApplicationConventionPlugin") {
            it.android.application
        }
        registerByClass("AndroidLibraryUIConventionPlugin") {
            it.android.library.ui
        }
        registerByClass("AndroidLibraryDataConventionPlugin") {
            it.android.library.data
        }
        registerByClass("AndroidNavigationConventionPlugin") {
            it.android.navigation
        }
        registerByClass("AndroidRoomConventionPlugin") {
            it.android.room
        }
        registerByClass("JvmLibraryConventionPlugin") {
            it.jvm.library
        }
        registerByClass("AndroidLocalesConventionPlugin") {
            it.android.locales
        }
        registerByClass("UIDependenciesConventionPlugin") {
            it.dependencies.ui
        }
        registerByClass("DataDependenciesConventionPlugin") {
            it.dependencies.data
        }
        registerByClass("DomainDependenciesConventionPlugin") {
            it.dependencies.domain
        }
    }
}