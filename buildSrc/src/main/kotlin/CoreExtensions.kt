import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.internal.catalog.DelegatingProjectDependency
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

val Provider<PluginDependency>.id get() = get().pluginId

internal fun DependencyHandler.add(name: String, vararg dependencies: Any)
    = dependencies.forEach { add(name, it) }

fun DependencyHandler.implementations(vararg dependencies: Any)
    = add("implementation", *dependencies)

fun DependencyHandler.apis(vararg dependencies: Any)
    = add("api", *dependencies)

operator fun <T : DelegatingProjectDependency> T.invoke(block: T.() -> Unit)= block(this)

