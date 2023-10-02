@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.ksp)
}

android {
    namespace = "lab.maxb.dark.data.users"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(mapOf("path" to ":core:domain")))
    implementation(project(":feature:users:domain"))
    implementation(project(mapOf("path" to ":core:data")))

    // Dependency Injection
    implementation(libs.koin.bom)
    implementation(libs.koin.android)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp)

    // Coroutines
    implementation(libs.coroutines.core)

    // Room persistence
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.ksp)
    implementation(libs.room.paging)

    // Retrofit2 Networking
    implementation(libs.retrofit.core)
    implementation(libs.gson)

    // Desugaring (Time-related features support for API 21+)
    coreLibraryDesugaring(libs.desugaring)

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit.core)
    androidTestImplementation(libs.junit)
}