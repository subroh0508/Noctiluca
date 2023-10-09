plugins {
    id("common-model")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // implementation(project(":core:data:account:model"))

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.status.model" }
