plugins {
    id("multiplatform-library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.domain" }
