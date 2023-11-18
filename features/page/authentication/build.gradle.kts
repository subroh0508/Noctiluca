plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:domain:authentication"))
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.core)
            }
        }
    }
}

android { namespace = "noctiluca.features.authentication" }
