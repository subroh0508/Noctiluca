plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:domain:authentication"))
                implementation(project(":features:shared"))
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
