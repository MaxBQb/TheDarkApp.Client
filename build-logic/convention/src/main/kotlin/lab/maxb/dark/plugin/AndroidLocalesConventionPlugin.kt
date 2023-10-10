@file:Suppress("unused")

package lab.maxb.dark.plugin

import com.android.build.gradle.LibraryExtension
import lab.maxb.dark.plugin.components.localProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


class AndroidLocalesConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        val rawLocales = getSupportedLocales()
        extensions.configure<LibraryExtension> {
            defaultConfig {
                resourceConfigurations += rawLocales
                buildConfigField("String[]", "LOCALES", wrapArray(rawLocales.map(::resToLoc)))
            }
        }
    }

    private fun Project.getSupportedLocales()
        = localProperties.getProperty("SUPPORTED_LOCALES")!!
        .split("[,;] ?".toRegex())
        .filter { it.isNotBlank() }

    private fun wrapArray(values: List<String>): String {
        return "{" + values.joinToString(", ") { "\"$it\"" } + "}"
    }

    private fun resToLoc(res: String) = res
        .replace("-r", "-")
        .replace("^b\\+".toRegex(), "")
        .replace("+", "-")
}
