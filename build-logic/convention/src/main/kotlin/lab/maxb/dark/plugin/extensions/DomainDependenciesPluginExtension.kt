@file:Suppress("unused")

package lab.maxb.dark.plugin.extensions

import lab.maxb.dark.plugin.common.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.project

open class DomainDependenciesPluginExtension(project: Project):
    Layer<DomainLayerFeatures, DomainLayerDependencyHandler>(project) {
    override val features = DomainLayerFeatures(project)
    override val dependencies get() = DomainLayerDependencyHandler(project, features)
}

class DomainLayerFeatures(project: Project) : LayerFeatures(project)
class DomainLayerDependencyHandler(project: Project, features: DomainLayerFeatures)
    : LayerDependencyHandler<DomainLayerFeatures>(project, features) {
    override fun koin(process: Boolean) {
        super.koin(process)
        implementation(libs.koin.core)
    }

    fun pagingFeature() {
        implementation(project(":feature:paging:domain"))
    }
}