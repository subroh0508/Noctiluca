plugins {
    id("common-domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":common:data:account:model"))
                api(project(":common:data:status:model"))
                implementation(project(":common:data:account:infra"))
                // implementation(project(":common:data:status:infra"))
            }
        }
    }
}
