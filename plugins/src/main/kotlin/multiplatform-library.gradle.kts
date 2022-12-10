import extension.Target
import extension.androidConfig
import extension.proguardConfig
import extension.targets
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    targets(Target.ANDROID, Target.DESKTOP)
}

android {
    androidConfig()
    proguardConfig()
}
