@file:Suppress("unused")

package lab.maxb.dark.plugin

import lab.maxb.dark.plugin.extensions.DomainDependenciesPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class DomainDependenciesConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        val extension = extensions.create<DomainDependenciesPluginExtension>(
            "domainModule", this
        )
    }
}
