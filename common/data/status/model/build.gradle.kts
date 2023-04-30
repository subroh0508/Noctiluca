plugins {
    id("common-model")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:data:account:model"))

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.status.model" }
