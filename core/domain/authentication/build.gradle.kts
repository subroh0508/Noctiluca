plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:model"))
                implementation(project(":core:data"))
            }
        }

        commonTest {
            dependencies {
                implementation(project(":core:network:authentication"))
                implementation(project(":core:network:instancessocial"))
            }
        }
    }
}

android { namespace = "noctiluca.authentication.domain" }
