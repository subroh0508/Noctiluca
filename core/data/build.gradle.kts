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
                implementation(project(":core:datastore"))
                implementation(project(":core:network:authentication"))
                implementation(project(":core:network:mastodon"))

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
    }
}

android { namespace = "noctiluca" }
