plugins {
    id("common-model")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.status.model" }
