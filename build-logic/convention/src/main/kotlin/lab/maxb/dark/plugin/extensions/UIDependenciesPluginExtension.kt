@file:Suppress("unused")

package lab.maxb.dark.plugin.extensions

import lab.maxb.dark.plugin.common.androidTestImplementation
import lab.maxb.dark.plugin.common.api
import lab.maxb.dark.plugin.common.id
import lab.maxb.dark.plugin.common.implementation
import lab.maxb.dark.plugin.common.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.project

open class UIDependenciesPluginExtension(project: Project):
    CommonAndroidDependenciesPluginExtension<UILayerFeatures, UILayerDependencyHandler>(project) {
    override val features = UILayerFeatures(project)
    override val dependencies get() = UILayerDependencyHandler(project, features)
}

open class UILayerFeatures(project: Project) : CommonAndroidLayerFeatures(project) {
    internal var hasNavigation by onceToggleable {
        project.plugins.apply(project.libs.plugins.convention.android.navigation.id)
    }
    var hasPaging by onceToggleable()
}

open class UILayerDependencyHandler(project: Project, features: UILayerFeatures)
    : CommonAndroidLayerDependencyHandler<UILayerFeatures>(project, features) {

    init {
        if (features.hasPaging) {
            paging()
            pagingFeature()
        }
    }

    fun navigation(process: Boolean = true) {
        api(project(":feature:navigation:api"))
        implementation(libs.compose.destinations.core)
        if (process) {
            ksp(libs.compose.destinations.ksp)
            features.hasNavigation = true
        }
    }

    fun paging() {
        implementation(libs.paging.runtime)
        implementation(libs.paging.compose)
        features.hasPaging = true
    }

    fun scalableUnits() {
        implementation(libs.scalable.dp)
        implementation(libs.scalable.sp)
    }

    fun compose() {
        implementation(platform(libs.compose.bom))
        implementation(libs.compose.runtime)
        implementation(libs.compose.ui.core)
        implementation(libs.compose.ui.util)
        implementation(libs.compose.ui.tooling)
        implementation(libs.compose.foundation.core)
        implementation(libs.compose.foundation.layout)
        implementation(libs.compose.material3)
        implementation(libs.compose.animation)
        implementation(libs.lifecycle.viewmodel.compose)

        androidTestImplementation(platform(libs.compose.bom))
        androidTestImplementation(libs.compose.ui.test)
    }

    fun images() {
        implementation(libs.landscapist.coil)
        implementation(libs.landscapist.animation)
    }

    override fun coroutines() {
        super.coroutines()
        implementation(libs.lifecycle.viewmodel.ktx)
        implementation(libs.lifecycle.runtime)
        implementation(libs.coroutines.android)
    }

    override fun koin(process: Boolean) {
        super.koin(process)
        implementation(libs.koin.compose)
    }

    fun espresso() {
        androidTestImplementation(libs.espresso.core)
    }

    override fun defaultDependencies(processKoin: Boolean) {
        super.defaultDependencies(processKoin)
        compose()
        scalableUnits()
    }

    fun pagingFeature() {
        api(project(":feature:paging:domain"))
        implementation(project(":feature:paging:data"))
    }
}