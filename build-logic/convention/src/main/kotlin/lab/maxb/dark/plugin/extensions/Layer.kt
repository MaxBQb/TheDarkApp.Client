@file:Suppress("unused")

package lab.maxb.dark.plugin.extensions

import lab.maxb.dark.plugin.common.id
import lab.maxb.dark.plugin.common.implementation
import lab.maxb.dark.plugin.common.libs
import lab.maxb.dark.plugin.common.testImplementation
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import kotlin.properties.Delegates


abstract class Layer<
    F : LayerFeatures,
    D : LayerDependencyHandler<F>
>(protected val project: Project) {
    abstract val dependencies : D
    abstract val features : F
    fun dependencies(block: Action<D>) = block.execute(dependencies)
    fun features(block: Action<F>) = block.execute(features)
}

open class LayerFeatures(protected val project: Project) {
    protected fun onceToggleable(onToggle: (() -> Unit)? = null)
        = Delegates.vetoable(false) { _, old, new ->
        if (new && old != new) {
            onToggle?.invoke()
            true
        } else false
    }

    var hasKsp by onceToggleable {
        project.plugins.apply(project.libs.plugins.ksp.id)
    }
}

abstract class LayerDependencyHandler<F : LayerFeatures> (
    val project: Project,
    protected val features: F,
) : DependencyHandler by project.dependencies {
    protected val libs = project.libs

    protected fun DependencyHandler.ksp(dependency: Any) {
        features.hasKsp = true
        add("ksp", dependency)
    }

    /**
     *  Dependency Injection
     *  @param process Use annotations and ksp to generate DI module
     * */
    open fun koin(process: Boolean = true) {
        implementation(platform(libs.koin.bom))

        if (process) {
            implementation(libs.koin.annotations)
            ksp(libs.koin.ksp)
        }

        testImplementation(libs.koin.test.core)
        testImplementation(libs.koin.test.junit5)
    }

    open fun coroutines() {
        implementation(libs.coroutines.core)
    }

    open fun tests() {
        testImplementation(libs.junit.core)
        testImplementation(libs.junit.jupiter.api)
    }

    open fun defaultDependencies(processKoin: Boolean = true) {
        koin(processKoin)
        coroutines()
        tests()
    }
}