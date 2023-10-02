@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.ksp)
}

android {
    namespace = "lab.maxb.dark.ui.auth"
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
    ksp {
        arg("compose-destinations.mode", "destinations")
        arg("compose-destinations.moduleName", "auth")
    }
}

dependencies {
    implementation(project(mapOf("path" to ":core:domain")))
    implementation(project(":feature:settings:domain"))
    implementation(project(":feature:auth:domain"))
    implementation(project(mapOf("path" to ":core:data")))
    implementation(project(mapOf("path" to ":core:ui")))
    implementation(project(mapOf("path" to ":core:components")))
    api(project(":feature:navigation:api"))

    // Dependency Injection
    implementation(libs.koin.bom)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp)

    // Coroutines
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // JC Dialogs
    implementation(libs.material.dialogs)

    // JC Navigation
    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)

    // Android X/KTX
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)

    // Scalable layout units
    implementation(libs.scalable.dp)
    implementation(libs.scalable.sp)

    // Desugaring (Time-related features support for API 21+)
    coreLibraryDesugaring(libs.desugaring)

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

    testImplementation(libs.junit.core)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}