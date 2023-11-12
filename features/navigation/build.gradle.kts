plugins {
    id("multiplatform-library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:model"))

                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.features.navigation" }
