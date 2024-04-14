plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.media3.common)
            }
        }
    }
}

android { namespace = "noctiluca.features.toot" }
