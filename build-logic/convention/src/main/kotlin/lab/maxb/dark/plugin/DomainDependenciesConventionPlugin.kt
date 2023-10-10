@file:Suppress("unused")

package lab.maxb.dark.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import lab.maxb.dark.plugin.extensions.DomainDependenciesPluginExtension

class DomainDependenciesConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        val extension = extensions.create<DomainDependenciesPluginExtension>(
            "domainModule", this
        )
    }
}
