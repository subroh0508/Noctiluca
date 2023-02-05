import extension.localProperties

plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
    alias(libs.plugins.buildconfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
                implementation(libs.bundles.ktor.client.common)
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

android { namespace = "noctiluca.api.instancessocial" }

buildConfig {
    useKotlinOutput()

    val apiToken = localProperties().getProperty("instances.social.token")

    buildConfigField("String", "SOCIAL_INSTANCES_API_TOKEN", "\"$apiToken\"")
}
