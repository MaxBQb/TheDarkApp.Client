@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.ksp)
}

android {
    namespace = "lab.maxb.dark.ui.navigation"
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
    implementation(project(mapOf("path" to ":core:ui")))
    implementation(project(mapOf("path" to ":feature:articles:ui")))
    implementation(project(mapOf("path" to ":feature:welcome:ui")))
    implementation(project(mapOf("path" to ":feature:settings:ui")))
    implementation(project(mapOf("path" to ":feature:auth:ui")))
    implementation(project(mapOf("path" to ":feature:tasks:ui:add")))
    implementation(project(mapOf("path" to ":feature:tasks:ui:solve")))
    implementation(project(mapOf("path" to ":feature:tasks:ui:list")))
    implementation(project(mapOf("path" to ":feature:navigation-drawer:ui")))
    api(project(":feature:navigation:api"))

    // Dependency Injection
    implementation(libs.koin.bom)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp)

    // Coroutines
    implementation(libs.coroutines.core)

    // JC Navigation
    implementation(libs.compose.destinations.core)

    // Android X/KTX
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)

    // Desugaring (Time-related features support for API 21+)
    coreLibraryDesugaring(libs.desugaring)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui.core)

    testImplementation(libs.junit.core)
    androidTestImplementation(libs.junit)
}