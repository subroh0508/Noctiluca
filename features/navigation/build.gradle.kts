plugins {
    id("multiplatform-library")
    id("org.jetbrains.compose")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:model"))

                implementation(compose.runtime)
                implementation(compose.foundation)

                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.features.navigation" }
