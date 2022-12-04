import extension.androidConfig
import extension.proguardConfig

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val androidMain by getting
        val androidTest by getting
        val desktopMain by getting
        val desktopTest by getting
    }
}

android {
    androidConfig()
    proguardConfig()
}

