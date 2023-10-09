plugins {
    id("features")
    id("has-resources")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:datastore"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.client.core)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.androidx.core)
                implementation(libs.androidx.browser)
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
    }
}

android { namespace = "noctiluca.features.components" }
