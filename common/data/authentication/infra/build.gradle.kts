plugins {
    id("common-infra")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:api:authentication"))
                implementation(project(":common:api:token"))
                implementation(project(":common:data:authentication:model"))

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
