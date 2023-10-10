@file:Suppress("unused")

package lab.maxb.dark.plugin

import com.android.build.api.dsl.ApplicationExtension
import lab.maxb.dark.plugin.common.ProjectConfig
import lab.maxb.dark.plugin.common.id
import lab.maxb.dark.plugin.common.libs
import lab.maxb.dark.plugin.components.configureAndroidCompose
import lab.maxb.dark.plugin.components.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        applyPlugins()
        setProjectConfig()
    }

    private fun Project.applyPlugins() = apply {
        plugin(libs.plugins.android.application.id)
        plugin(libs.plugins.kotlin.android.id)
        plugin(libs.plugins.ksp.id)
    }

    private fun Project.setProjectConfig() {
        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)
            configureAndroidCompose(this)
            defaultConfig {
                targetSdk = ProjectConfig.compileSdk
                applicationId = ProjectConfig.applicationId
                versionCode = ProjectConfig.version.code
                versionName = ProjectConfig.version.name
            }
            sourceSets.all {
                java.srcDirs("src/$name/kotlin")
            }
        }
    }
}
