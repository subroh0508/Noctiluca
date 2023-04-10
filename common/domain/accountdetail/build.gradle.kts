plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":common:data:account:model"))
                api(project(":common:data:accountdetail:model"))
                api(project(":common:data:status:model"))
                implementation(project(":common:data:accountdetail:infra"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

android { namespace = "noctiluca.accountdetail.domain" }
