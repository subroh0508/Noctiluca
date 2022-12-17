plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:data:authentication:model"))
                implementation(project(":common:data:authentication:infra"))
            }
        }
    }
}
