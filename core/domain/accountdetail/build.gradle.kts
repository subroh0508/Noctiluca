plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:data:account:model"))
                api(project(":core:data:accountdetail:model"))
                api(project(":core:data:status:model"))
                implementation(project(":core:data:accountdetail:infra"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

android { namespace = "noctiluca.accountdetail.domain" }
