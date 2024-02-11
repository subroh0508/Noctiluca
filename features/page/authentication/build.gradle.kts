plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.androidx.core)
            }
        }
    }
}

android { namespace = "noctiluca.features.authentication" }
