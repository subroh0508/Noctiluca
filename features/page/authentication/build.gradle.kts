plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:domain:authentication"))
                implementation(project(":features:shared"))
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
