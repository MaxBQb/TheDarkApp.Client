// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.secrets.gradle.plugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.dependencyAnalysis)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://www.jitpack.io")
            setUrl("https://kotlin.bintray.com/kotlinx")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
