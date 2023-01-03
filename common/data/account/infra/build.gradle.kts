plugins {
    id("common-infra")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:api:mastodon"))
                implementation(project(":common:data:account:model"))
            }
        }
    }
}
