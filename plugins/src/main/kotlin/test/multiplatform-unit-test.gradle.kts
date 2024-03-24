package test

import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("io.kotest.multiplatform")
}

tasks.named<Test>(TASK_TEST_DESKTOP_TEST) { jvmConfig() }
tasks.named<KotlinNativeSimulatorTest>(TASK_TEST_IOS_X64_TEST) { config() }
tasks.named<KotlinNativeSimulatorTest>(TASK_TEST_IOS_SIMULATOR_ARM64_TEST) { config() }

tasks.register(TASK_TEST_IOS_TEST) {
    group = "Verification"
    description = "An alias for iosX64Test if running in CI; otherwise iosSimulatorArm64Test."

    dependsOn(
        if (System.getenv("CI") != null) {
            TASK_TEST_IOS_X64_TEST
        } else {
            TASK_TEST_IOS_SIMULATOR_ARM64_TEST
        },
    )
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

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
