plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:data"))
                implementation(project(":core:model"))
                api(project(":core:data:status:model"))
                api(project(":core:data:timeline:model"))
                implementation(project(":core:data:status:infra"))
                implementation(project(":core:data:timeline:infra"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

android { namespace = "noctiluca.timeline.domain" }
