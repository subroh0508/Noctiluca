plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:data:authentication:model"))
                api(project(":core:data:instance:model"))
                api(project(":core:data:status:model"))
                implementation(project(":core:data:authentication:infra"))
                implementation(project(":core:data:instance:infra"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":core:api:authentication"))
                implementation(project(":core:api:instancessocial"))
            }
        }
    }
}

android { namespace = "noctiluca.authentication.domain" }
