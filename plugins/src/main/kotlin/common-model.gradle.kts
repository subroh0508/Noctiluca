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
        named("commonMain")
        named("commonTest")
        named("androidMain")
        named("androidTest")
        named("desktopMain")
        named("desktopTest")
    }
}

android {
    androidConfig()
    proguardConfig()
}

