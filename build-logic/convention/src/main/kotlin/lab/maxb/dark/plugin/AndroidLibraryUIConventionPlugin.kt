@file:Suppress("unused")

package lab.maxb.dark.plugin

import com.android.build.gradle.LibraryExtension
import lab.maxb.dark.plugin.common.id
import lab.maxb.dark.plugin.common.libs
import lab.maxb.dark.plugin.components.configureAndroidCompose
import lab.maxb.dark.plugin.components.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


class AndroidLibraryUIConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        applyPlugins()
        setProjectConfig()
    }

    private fun Project.applyPlugins() = apply {
        plugin(libs.plugins.android.library.id)
        plugin(libs.plugins.kotlin.android.id)
    }

    private fun Project.setProjectConfig() {
        extensions.configure<LibraryExtension> {
            configureKotlinAndroid(this)
            configureAndroidCompose(this)
        }
    }
}
