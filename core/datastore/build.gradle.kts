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
                implementation(libs.okio.fakefilesystem)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
            }
        }

        androidUnitTest {
            dependencies {
                implementation(libs.kotest.runner.junit5)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        desktopTest {
            dependencies {
                implementation(libs.kotest.runner.junit5)
            }
        }
    }
}

android { namespace = "noctiluca.datastore" }
