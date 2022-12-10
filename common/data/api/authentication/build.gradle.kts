plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.bundles.ktor.client.common)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.bundles.ktor.client.jvm)
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.bundles.ktor.client.jvm)
            }
        }
    }
}
