plugins {
    id("common-infra")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:api:authentication"))
                implementation(project(":core:api:token"))
                implementation(project(":core:data:authentication:model"))

                implementation(libs.kotlinx.coroutines.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}

android { namespace = "noctiluca.authentication.infra" }
