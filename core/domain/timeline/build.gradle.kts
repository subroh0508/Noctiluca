plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:data:account:model"))
                api(project(":core:data:status:model"))
                api(project(":core:data:timeline:model"))
                implementation(project(":core:data:account:infra"))
                implementation(project(":core:data:status:infra"))
                implementation(project(":core:data:timeline:infra"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

android { namespace = "noctiluca.timeline.domain" }
