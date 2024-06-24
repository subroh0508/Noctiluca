package test

import extension.kotestAssertionsCore
import extension.kotestFrameworkDataTest
import extension.kotestFrameworkEngine
import extension.kotestRunnerJunit5
import extension.ktorClientMock
import extension.ktorClientResources
import extension.libs
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("io.kotest.multiplatform")
}

tasks.named<Test>(TASK_TEST_DESKTOP_TEST) { jvmConfig() }
tasks.named<KotlinNativeSimulatorTest>(TASK_TEST_IOS_SIMULATOR_ARM64_TEST) { config() }

tasks.register(TASK_TEST_IOS_TEST) {
    group = "Verification"
    description = "An alias for iosX64Test if running in CI; otherwise iosSimulatorArm64Test."

    dependsOn(TASK_TEST_IOS_SIMULATOR_ARM64_TEST)
}

android {
    testOptions.unitTests {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
        all { it.jvmConfig() }
    }
}

fun Test.jvmConfig() {
    useJUnitPlatform()
    config()
}

fun AbstractTestTask.config() {
    reports.junitXml.required.set(true)
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED)
    }
}

kotlin {
    sourceSets {
        commonTest {
            dependencies {
                implementation(project(":core:test:shared"))

                implementation(libs.ktorClientResources)
                implementation(libs.ktorClientMock)
                implementation(libs.kotestAssertionsCore)
                implementation(libs.kotestFrameworkEngine)
                implementation(libs.kotestFrameworkDataTest)
            }
        }
        named("androidUnitTest") {
            dependencies {
                implementation(libs.kotestRunnerJunit5)
            }
        }
        named("desktopTest") {
            dependencies {
                implementation(libs.kotestRunnerJunit5)
            }
        }
    }
}
