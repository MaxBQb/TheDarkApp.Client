package lab.maxb.dark.plugin.common

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.the
import org.gradle.plugin.use.PluginDependency

val Project.libs get() = the<LibrariesForLibs>()

val Provider<PluginDependency>.id get() = get().pluginId

internal fun DependencyHandler.api(dependency: Any)
    = add("api", dependency)

internal fun DependencyHandler.implementation(dependency: Any)
    = add("implementation", dependency)

internal fun DependencyHandler.testImplementation(dependency: Any)
    = add("testImplementation", dependency)

internal fun DependencyHandler.androidTestImplementation(dependency: Any)
    = add("androidTestImplementation", dependency)
