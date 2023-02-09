import extension.*
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("multiplatform-library")
    id("common-model")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(libs.koinCore)
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotestAssertionsCore)
                implementation(libs.kotestFrameworkEngine)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.koinAndroid)
            }
        }
        named("androidTest") {
            dependencies {
                implementation(libs.kotestRunnerJunit5)
            }
        }
        named("desktopMain")
        named("desktopTest") {
            dependencies {
                implementation(libs.kotestRunnerJunit5)
            }
        }
    }
}

tasks.named<Test>("desktopTest") {
    useJUnitPlatform()
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED)
    }
}

android {
    testOptions.unitTests.all {
        it.useJUnitPlatform()
        it.testLogging {
            showExceptions = true
            showStandardStreams = true
            events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED)
        }
    }
}
