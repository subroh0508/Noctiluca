import extension.*
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("android")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.android.application")
    id("kotlin-parcelize")
}

android {
    androidApplicationConfig()
    proguardApplicationConfig()
}

dependencies {
    coreLibraryDesugaring(libs.androidDesugarjdk)
}
