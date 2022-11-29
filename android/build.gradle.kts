plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group "net.subroh0508"
version "1.0-SNAPSHOT"

dependencies {
    implementation(project(":common"))
    implementation(libs.androidx.activity.compose)
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "net.subroh0508.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}