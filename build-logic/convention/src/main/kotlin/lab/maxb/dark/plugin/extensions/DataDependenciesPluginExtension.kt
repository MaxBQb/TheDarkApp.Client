@file:Suppress("unused")

package lab.maxb.dark.plugin.extensions

import lab.maxb.dark.plugin.common.api
import lab.maxb.dark.plugin.common.id
import lab.maxb.dark.plugin.common.implementation
import lab.maxb.dark.plugin.common.libs
import lab.maxb.dark.plugin.common.runtimeOnly
import org.gradle.api.Project
import org.gradle.kotlin.dsl.project

open class DataDependenciesPluginExtension(project: Project):
    CommonAndroidDependenciesPluginExtension<DataLayerFeatures, DataLayerDependencyHandler>(project) {
    override val features = DataLayerFeatures(project)
    override val dependencies get() = DataLayerDependencyHandler(project, features)
}

open class DataLayerFeatures(project: Project) : CommonAndroidLayerFeatures(project) {
    var hasPaging by onceToggleable()
    internal var hasSerializationKtx by onceToggleable {
        project.plugins.apply(project.libs.plugins.kotlin.serialization.id)
    }
    internal var hasRoom by onceToggleable {
        project.plugins.apply(project.libs.plugins.convention.android.room.id)
    }
}
open class DataLayerDependencyHandler(project: Project, features: DataLayerFeatures)
    : CommonAndroidLayerDependencyHandler<DataLayerFeatures>(project, features) {
    init {
        if (features.hasPaging) {
            paging()
            pagingFeature()
        }
    }

    fun paging() {
        implementation(libs.paging.runtime)
        features.hasPaging = true
    }

    fun room(paging: Boolean = features.hasPaging) {
        runtimeOnly(libs.room.runtime)
        implementation(libs.room.ktx)
        features.hasRoom = true
        ksp(libs.room.ksp)
        if (paging)
            implementation(libs.room.paging)
    }

    fun retrofit() {
        implementation(libs.retrofit.core)
        jsonGson()
    }

    fun dataStore(encrypted: Boolean = false) {
        implementation(libs.datastore.preferences)
        if (encrypted)
            implementation(libs.datastore.encrypted)
        jsonKtx()
    }

    fun jsonKtx() {
        features.hasSerializationKtx = true
        implementation(libs.kotlinx.serialization.json)
    }

    fun jsonGson() {
        implementation(libs.gson)
    }

    fun pagingFeature() {
        api(project(":feature:paging:domain"))
        implementation(project(":feature:paging:data"))
    }
}