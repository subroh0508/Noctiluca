plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:model"))
                implementation(project(":core:data"))
                api(project(":core:data:instance:model"))
                implementation(project(":core:data:instance:infra"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":core:network:authentication"))
                implementation(project(":core:api:instancessocial"))
            }
        }
    }
}

android { namespace = "noctiluca.authentication.domain" }
