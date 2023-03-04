plugins {
    id("common-infra")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:api:mastodon"))
                implementation(project(":common:api:token"))
                implementation(project(":common:data:account:model"))

                implementation(libs.kotlinx.serialization)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}

android { namespace = "noctiluca.account.infra" }
