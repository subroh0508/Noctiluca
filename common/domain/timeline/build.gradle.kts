plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":common:data:account:model"))
                api(project(":common:data:status:model"))
                api(project(":common:data:timeline:model"))
                implementation(project(":common:data:account:infra"))
                implementation(project(":common:data:status:infra"))
                implementation(project(":common:data:timeline:infra"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

android { namespace = "noctiluca.timeline.domain" }
