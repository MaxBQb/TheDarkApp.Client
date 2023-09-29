@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "lab.maxb.dark.ui.components"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(project(mapOf("path" to ":core:data")))

    // Dependency Injection
    implementation(libs.koin.bom)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Coroutines
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // JC Dialogs
    implementation(libs.material.dialogs)

    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // JC Navigation
    implementation(libs.compose.destinations.core)

    // Android X/KTX
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)

    // Scalable layout units
    implementation(libs.scalable.dp)
    implementation(libs.scalable.sp)
    implementation(project(mapOf("path" to ":core:ui")))
    implementation(project(mapOf("path" to ":feature:navigation-drawer:ui")))

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
    implementation(libs.compose.material3)
    implementation(libs.compose.animation)
    implementation(libs.lifecycle.viewmodel.compose)

    // JC Accompanist
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.adaptive)

    testImplementation(libs.junit.core)
    androidTestImplementation(libs.junit)
}