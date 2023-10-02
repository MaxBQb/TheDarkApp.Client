import java.io.FileInputStream
import java.util.*

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.ksp)
}

android {
    namespace = "lab.maxb.dark.data"
    compileSdk = 34

    val prop = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "keystore.properties")))
    }

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // URL for main API
        buildConfigField("String", "DARK_API_URL", prop.getProperty("DARK_API_URL"))
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
    implementation(project(mapOf("path" to ":core:data")))
    implementation(project(mapOf("path" to ":feature:paging:data")))
    api(project(":feature:articles:data"))
    api(project(":feature:auth:data"))
    api(project(":feature:tasks:data"))
    api(project(":feature:users:data"))

    // Dependency Injection
    implementation(libs.koin.bom)
    implementation(libs.koin.android)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp)

    // Coroutines
    implementation(libs.coroutines.core)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Datastore
    implementation(libs.datastore.encrypted)

    // Room persistence
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.ksp)
    implementation(libs.room.paging)

    // Retrofit2 Networking
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)
    implementation(libs.logging.interceptor)

    // Desugaring (Time-related features support for API 21+)
    coreLibraryDesugaring(libs.desugaring)

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit.core)
    androidTestImplementation(libs.junit)
}