@file:Suppress("unused")

package lab.maxb.dark.plugin

import lab.maxb.dark.plugin.extensions.UIDependenciesPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class UIDependenciesConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        val extension = extensions.create<UIDependenciesPluginExtension>(
            "uiModule", this
        )
    }
}
