import extension.*
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("android")
    id("org.jetbrains.compose")
    id("com.android.application")
}

android {
    androidApplicationConfig()
    proguardApplicationConfig()
}
