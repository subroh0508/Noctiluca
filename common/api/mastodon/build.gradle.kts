plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:data:shared"))

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

android { namespace = "noctiluca.api.mastodon" }
