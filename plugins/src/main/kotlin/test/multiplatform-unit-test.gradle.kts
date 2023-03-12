package test

import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

tasks.named<Test>(TASK_TEST_DESKTOP_TEST) { config() }

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions.unitTests {
        isIncludeAndroidResources = true
        all { it.config() }
    }
}

fun Test.config() {
    useJUnitPlatform()
    reports.junitXml.required.set(true)
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED)
    }
}
