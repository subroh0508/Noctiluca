package test

import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

tasks.named<Test>(TASK_TEST_DESKTOP_TEST) { jvmConfig() }
tasks.named<KotlinNativeTest>(TASK_TEST_IOS_TEST) { config() }

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
