plugins {
    id("common-data")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:model"))
                implementation(project(":core:datastore"))
                implementation(project(":core:network:authorization"))
                implementation(project(":core:network:mastodon"))
                implementation(project(":core:network:instancessocial"))

                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

android { namespace = "noctiluca" }
