plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:network:mastodon"))
                implementation(project(":core:data"))
                implementation(project(":core:datastore"))
                implementation(project(":core:model"))

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.resources)
                implementation(libs.ktor.client.mock)
                implementation(libs.koin.core)
                implementation(libs.kotest.framework.engine)
            }
        }
    }
}

android { namespace = "noctiluca.test.shared" }
