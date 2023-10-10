@file:Suppress("unused")

package lab.maxb.dark.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import lab.maxb.dark.plugin.extensions.UIDependenciesPluginExtension

class UIDependenciesConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        val extension = extensions.create<UIDependenciesPluginExtension>(
            "uiModule", this
        )
    }
}
