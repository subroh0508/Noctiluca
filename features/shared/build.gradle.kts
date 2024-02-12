plugins {
    id("features")
    id("has-resources")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:data"))
                implementation(project(":core:model"))
                implementation(project(":features:navigation"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.client.core)

                implementation(libs.image.loader)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.koin)

                implementation(libs.koin.core)
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

android { namespace = "noctiluca.features.shared" }
