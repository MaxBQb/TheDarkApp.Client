package lab.maxb.dark.plugin.components

import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.Properties

internal fun Project.getProperties(fileName: String) = Properties().apply {
    load(FileInputStream(File(project.rootProject.rootDir, fileName)))
}

internal val Project.localProperties get() = getProperties("local.properties")