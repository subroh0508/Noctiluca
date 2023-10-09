plugins {
    id("common-infra")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:api:mastodon"))
                implementation(project(":core:datastore"))
                implementation(project(":core:data:account:model"))

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
