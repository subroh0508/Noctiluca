plugins {
    id("common-infra")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:api:instancessocial"))
                implementation(project(":core:api:mastodon"))
                implementation(project(":core:data:instance:model"))
                implementation(project(":core:data:status:model"))
                implementation(project(":core:data:status:infra"))

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

android { namespace = "noctiluca.instance.infra" }
