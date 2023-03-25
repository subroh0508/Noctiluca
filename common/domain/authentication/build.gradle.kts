plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":common:data:authentication:model"))
                api(project(":common:data:instance:model"))
                implementation(project(":common:data:authentication:infra"))
                implementation(project(":common:data:instance:infra"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":common:api:authentication"))
                implementation(project(":common:api:instancessocial"))
            }
        }
    }
}

android { namespace = "noctiluca.authentication.domain" }
