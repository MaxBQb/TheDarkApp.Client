import java.io.FileInputStream
import java.util.*

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.ksp)
}


val supportedLocales = arrayOf("en", "ru")
fun resToLoc(res: String) = res
    .replace("-r", "-")
    .replace("^b\\+".toRegex(), "")
    .replace("+", "-")


android {
    namespace = "lab.maxb.dark.ui.core"
    compileSdk = 34

    val prop = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "keystore.properties")))
    }

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        resourceConfigurations += supportedLocales
        buildConfigField(
            "String[]", "LOCALES",
            "{\"" + supportedLocales.joinToString("\",\"") { resToLoc(it) } + "\"}"
        )

        // URL for code repository
        buildConfigField("String", "APP_REPOSITORY", prop.getProperty("APP_REPOSITORY"))
    }

    buildFeatures {
        compose = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

dependencies {
    implementation(project(":core:domain"))

    // Dependency Injection
    implementation(libs.koin.bom)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Coroutines
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Paging
    implementation(libs.paging.runtime)

    // JC Navigation
    implementation(libs.compose.destinations.core)

    // Android X/KTX
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)

    // Scalable layout units
    implementation(libs.scalable.dp)
    implementation(libs.scalable.sp)

    // Desugaring (Time-related features support for API 21+)
    coreLibraryDesugaring(libs.desugaring)

    // Images with Coil by landscapist JC version
    implementation(libs.landscapist.coil)
    implementation(libs.landscapist.animation)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui.core)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.foundation.core)
    implementation(libs.compose.foundation.layout)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.theme.adapter)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.animation)
    implementation(libs.lifecycle.viewmodel.compose)

    testImplementation(libs.junit.core)
    androidTestImplementation(libs.junit)
}