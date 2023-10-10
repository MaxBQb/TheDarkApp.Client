package lab.maxb.dark.plugin.components

import lab.maxb.dark.plugin.common.ProjectConfig
import org.gradle.api.Project


val Project.moduleName
    get(): String {
        val dataLayer = "data"
        val uiLayer = "ui"
        val featureKey = "feature"
        val coreKey = "core"
        return path.split(":")
            .dropWhile { it == featureKey || it.isBlank() }
            .filterNot { it == dataLayer || it == uiLayer }.let { list ->
                if (list.size > 1)
                    list.dropWhile { it == coreKey }
                else list
            }.joinToString(".").replace("-", "_")
    }

fun Project.namespace(): String {
    val dataLayer = "data"
    val uiLayer = "ui"
    val type = if (dataLayer in path.split(":")) dataLayer else uiLayer
    return "${ProjectConfig.applicationId}.$type.$moduleName".trimEnd('.')
}