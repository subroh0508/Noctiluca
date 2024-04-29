package test

import extension.androidxComposeUiTestJunit4Android
import extension.androidxComposeUiTestManifest
import extension.androidxTestCore
import extension.androidxTestJunit
import extension.androidxTestRunner
import extension.junit
import extension.junitVintage
import extension.libs
import extension.robolectric
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

// @see: https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html#writing-and-running-tests-with-compose-multiplatform
kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)

            dependencies {
                implementation(libs.androidxComposeUiTestJunit4Android)
                debugImplementation(libs.androidxComposeUiTestManifest)
            }
        }
    }

    sourceSets {
        commonTest {
            dependencies {
                // implementation(project(":core:test:ui"))

                implementation(kotlin("test"))
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
            }
        }

        named("androidUnitTest") {
            dependencies {
                // implementation(libs.junit)
                // implementation(libs.junitVintage)
                // implementation(libs.androidxTestCore)
                // implementation(libs.androidxTestRunner)
                // implementation(libs.androidxTestJunit)
                // implementation(libs.robolectric)
            }
        }

        named("desktopTest") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.desktop.uiTestJUnit4)
            }
        }
    }
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
