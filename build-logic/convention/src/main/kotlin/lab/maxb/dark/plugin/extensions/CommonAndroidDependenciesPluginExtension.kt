package lab.maxb.dark.plugin.extensions

import lab.maxb.dark.plugin.common.androidTestImplementation
import lab.maxb.dark.plugin.common.implementation
import org.gradle.api.Project

abstract class CommonAndroidDependenciesPluginExtension<
    F: CommonAndroidLayerFeatures,
    D: CommonAndroidLayerDependencyHandler<F>,
>(project: Project): Layer<F, D>(project)

abstract class CommonAndroidLayerFeatures(project: Project) : LayerFeatures(project)
abstract class CommonAndroidLayerDependencyHandler<T: CommonAndroidLayerFeatures>(project: Project, features: T)
    : LayerDependencyHandler<T>(project, features) {
    override fun koin(process: Boolean) {
        super.koin(process)
        implementation(libs.koin.android)
    }

    open fun androidX() {
        implementation(libs.androidx.core)
        implementation(libs.androidx.appcompat)
    }

    override fun tests() {
        super.tests()
        androidTestImplementation(libs.test.junit.android)
    }

    override fun defaultDependencies(
        processKoin: Boolean,
    ) {
        super.defaultDependencies(processKoin)
        androidX()
    }
}