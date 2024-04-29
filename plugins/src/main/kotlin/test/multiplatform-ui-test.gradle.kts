package test

import extension.androidxComposeUiTestJunit4Android
import extension.androidxComposeUiTestManifest
import extension.libs
import gradle.kotlin.dsl.accessors._7f7b60e80ee47e394210123ef9b27b8b.android
import gradle.kotlin.dsl.accessors._7f7b60e80ee47e394210123ef9b27b8b.compose
import gradle.kotlin.dsl.accessors._7f7b60e80ee47e394210123ef9b27b8b.debugImplementation
import gradle.kotlin.dsl.accessors._7f7b60e80ee47e394210123ef9b27b8b.implementation
import gradle.kotlin.dsl.accessors._7f7b60e80ee47e394210123ef9b27b8b.sourceSets
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    kotlin("multiplatform")
    id("com.android.library")
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
                implementation(kotlin("test"))
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
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
