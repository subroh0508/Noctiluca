plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization)

                implementation(libs.koin.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}

android { namespace = "noctiluca" }
