plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:data:shared"))

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.resources)
                implementation(libs.koin.core)
            }
        }
    }
}

android { namespace = "noctiluca.test" }
