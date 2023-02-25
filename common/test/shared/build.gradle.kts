plugins {
    id("multiplatform-library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:data:shared"))

                implementation(libs.koin.core)
            }
        }
    }
}

android { namespace = "noctiluca.test" }
