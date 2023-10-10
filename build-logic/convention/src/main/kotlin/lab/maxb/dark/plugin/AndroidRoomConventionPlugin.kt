@file:Suppress("unused")

package lab.maxb.dark.plugin

import com.google.devtools.ksp.gradle.KspExtension
import lab.maxb.dark.plugin.common.id
import lab.maxb.dark.plugin.common.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        applyPlugins()
        setProjectConfig()
    }

    private fun Project.applyPlugins() = apply {
        plugin(libs.plugins.ksp.id)
    }

    private fun Project.setProjectConfig() {
        extensions.configure<KspExtension> {
            arg("room.schemaLocation", "${projectDir}/schemas")
            arg("room.incremental", "true")
        }
    }
}
