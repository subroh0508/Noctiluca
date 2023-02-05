plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:domain:authentication"))
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.navigation.compose)
            }
        }
    }
}

android { namespace = "noctiluca.features.authentication" }
