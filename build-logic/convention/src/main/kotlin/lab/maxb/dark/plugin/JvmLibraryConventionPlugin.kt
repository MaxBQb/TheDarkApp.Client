@file:Suppress("unused")

package lab.maxb.dark.plugin

import lab.maxb.dark.plugin.common.id
import lab.maxb.dark.plugin.common.libs
import lab.maxb.dark.plugin.components.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        applyPlugins()
        setProjectConfig()
    }

    private fun Project.applyPlugins() = apply {
        plugin(libs.plugins.jvm.library.id)
        plugin(libs.plugins.kotlin.jvm.id)
    }

    private fun Project.setProjectConfig() {
        configureKotlinJvm()
    }
}