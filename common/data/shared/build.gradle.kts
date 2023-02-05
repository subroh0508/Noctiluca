plugins {
    id("multiplatform-library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koin.core)
            }
        }
    }
}

android { namespace = "noctiluca.model" }
