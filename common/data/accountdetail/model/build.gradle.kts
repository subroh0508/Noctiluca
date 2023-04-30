plugins {
    id("common-model")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:data:account:model"))
            }
        }
    }
}

android { namespace = "noctiluca.accountdetail.model" }
