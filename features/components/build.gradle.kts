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
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
    }
}
