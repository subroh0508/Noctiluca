plugins {
    id("common-infra")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:api:instancessocial"))
                implementation(project(":common:api:mastodon"))
                implementation(project(":common:data:instance:model"))
                implementation(project(":common:data:status:model"))
                implementation(project(":common:data:status:infra"))

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
