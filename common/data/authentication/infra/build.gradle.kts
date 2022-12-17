plugins {
    id("common-infra")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:api:authentication"))
                implementation(project(":common:data:authentication:model"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.datastore.core)
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}
