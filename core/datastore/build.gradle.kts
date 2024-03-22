plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
    id("test.multiplatform-unit-test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:model"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization)

                implementation(libs.androidx.datastore.core)
                implementation(libs.androidx.datastore.core.okio)
                implementation(libs.androidx.datastore.preferences.core)

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
                implementation(libs.koin.android)
            }
        }

        androidUnitTest {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.kotest.runner.junit5)
                implementation(libs.junit.core)
                implementation(libs.junit.vintage)
                implementation(libs.robolectric)
                implementation(libs.kotlinx.coroutines.test)
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

android { namespace = "noctiluca.datastore" }
