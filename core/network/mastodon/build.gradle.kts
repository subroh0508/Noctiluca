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

                implementation(libs.bundles.ktor.client.common)
                implementation(libs.ktor.client.websockets)
                implementation(libs.ktor.client.resources)
                implementation(libs.koin.core)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.bundles.ktor.client.jvm)
                implementation(libs.koin.android)
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.bundles.ktor.client.jvm)
            }
        }
    }
}

android { namespace = "noctiluca.network.mastodon" }
