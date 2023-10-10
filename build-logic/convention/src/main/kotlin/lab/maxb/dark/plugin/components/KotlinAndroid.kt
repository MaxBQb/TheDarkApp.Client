package lab.maxb.dark.plugin.components

import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import lab.maxb.dark.plugin.common.ProjectConfig
import lab.maxb.dark.plugin.common.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = ProjectConfig.compileSdk
        namespace = project.namespace()

        defaultConfig {
            minSdk = ProjectConfig.minSdk
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            // Up to Java 11 APIs are available through desugaring
            // https://developer.android.com/studio/write/java11-minimal-support-table
            sourceCompatibility = ProjectConfig.javaVersion
            targetCompatibility = ProjectConfig.javaVersion
            isCoreLibraryDesugaringEnabled = true
        }

        configureMinification(this)
        configureConsumer(this)

        testOptions {
            unitTests.all {
                it.useJUnitPlatform()
            }
        }
    }

    configureKotlin(true)

    dependencies {
        add("coreLibraryDesugaring", libs.desugaring)
    }
}

private fun configureConsumer(commonExtension: CommonExtension<*, *, *, *, *>) {
    if (commonExtension !is LibraryExtension)
        return
    commonExtension.defaultConfig.consumerProguardFiles("consumer-rules.pro")
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm(android: Boolean = false) {
    extensions.configure<JavaPluginExtension> {
        // Up to Java 11 APIs are available through desugaring
        // https://developer.android.com/studio/write/java11-minimal-support-table
        sourceCompatibility = ProjectConfig.javaVersion
        targetCompatibility = ProjectConfig.javaVersion
    }

    configureKotlin(android)
}

private fun <T : KotlinProjectExtension> Project.setupToolchain() {
    extensions.configure<T>("kotlin") {
        jvmToolchain(ProjectConfig.javaVersion.majorVersion.toInt())
    }
}

/**
 * Configure base Kotlin options
 */
private fun Project.configureKotlin(android: Boolean) {
    if (android)
        setupToolchain<KotlinAndroidProjectExtension>()
    else
        setupToolchain<KotlinJvmProjectExtension>()

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
            freeCompilerArgs += "-Xcontext-receivers"
        }
    }
}

internal fun <BuildTypeT : BuildType> configureMinification(
    commonExtension: CommonExtension<*, BuildTypeT, *, *, *>,
) {
    commonExtension.apply {
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
}