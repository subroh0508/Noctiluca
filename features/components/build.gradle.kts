plugins {
    id("features")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktor.client.core)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
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
