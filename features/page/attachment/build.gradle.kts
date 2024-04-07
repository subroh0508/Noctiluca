plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.image.loader)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.media3.exoplayer.core)
                implementation(libs.androidx.media3.ui)
                implementation(libs.androidx.media3.exoplayer.dash)
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.vlcj)
            }
        }
    }
}

android { namespace = "noctiluca.features.attachment" }
