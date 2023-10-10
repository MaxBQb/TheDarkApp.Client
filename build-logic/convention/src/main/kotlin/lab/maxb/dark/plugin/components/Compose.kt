package lab.maxb.dark.plugin.components

import com.android.build.api.dsl.CommonExtension
import lab.maxb.dark.plugin.common.libs
import org.gradle.api.Project

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.compose.asProvider().get().toString()
        }
    }
}