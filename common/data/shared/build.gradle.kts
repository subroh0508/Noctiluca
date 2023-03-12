plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
    id("test.multiplatform-unit-test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization)

                implementation(libs.koin.core)

                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.engine)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.datastore.preferences)
            }
        }

        androidTest {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.kotest.runner.junit5)
                implementation(libs.junit)
                implementation(libs.robolectric)
                implementation(libs.androidx.test.core)
                implementation(libs.androidx.test.runner)
                implementation(libs.androidx.test.junit)
            }
        }

        desktopTest {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.kotest.runner.junit5)
            }
        }
    }
}

android { namespace = "noctiluca" }
