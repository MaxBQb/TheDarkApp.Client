@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.ksp)
}

android {
    namespace = "lab.maxb.dark.ui.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "lab.maxb.dark"
        minSdk = 21
        targetSdk = 34
        versionCode = 4
        versionName = "0.4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
                argument("room.incremental", "true")
            }
        }
    }

    buildFeatures{
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
        kotlinCompilerExtensionVersion = libs.versions.compose.asProvider().get()
    }
    kotlin {
        jvmToolchain(11)
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xcontext-receivers"
    }
    applicationVariants.all {
        outputs.all {
            this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            outputFileName = "DarkApp.apk"
        }
    }
    sourceSets.all {
        java.srcDirs("src/$name/kotlin")
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":feature:articles:ui"))
    implementation(project(":feature:welcome:ui"))
    implementation(project(":feature:navigation:impl"))
    implementation(project(":feature:settings:ui"))
    implementation(project(":feature:auth:ui"))
    implementation(project(":feature:tasks:ui:add"))
    implementation(project(":feature:tasks:ui:solve"))
    implementation(project(":feature:tasks:ui:list"))
    implementation(project(":feature:navigation-drawer:ui"))
    implementation(project(":feature:articles:data"))
    implementation(project(":feature:data"))
    implementation(project(":feature:auth:data"))
    implementation(project(":feature:settings:data"))
    implementation(project(":feature:tasks:data"))
    implementation(project(":feature:users:data"))
    implementation(project(":feature:paging:data"))
    implementation(project(":feature:settings:domain"))
    implementation(project(":feature:tasks:domain"))
    implementation(project(":feature:articles:domain"))
    implementation(project(":feature:users:domain"))

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

    // Tests
    testImplementation(libs.junit.core)
    testImplementation(libs.junit.jupiter.api)

    // Koin
    testImplementation(libs.koin.test.core)
    testImplementation(libs.koin.test.junit5)

    // JetpackCompose UI Tests
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test)
}