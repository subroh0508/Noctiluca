plugins {
    id("common-infra")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:api:mastodon"))
                implementation(project(":core:api:token"))
                implementation(project(":core:data:account:model"))
                implementation(project(":core:data:accountdetail:model"))
                implementation(project(":core:data:status:model"))
                implementation(project(":core:data:status:infra"))

                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.datetime)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}

android { namespace = "noctiluca.accountdetail.infra" }
