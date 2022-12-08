import extension.androidConfig
import extension.proguardConfig
import extension.Target
import extension.targets

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    targets(Target.ANDROID, Target.DESKTOP)

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

