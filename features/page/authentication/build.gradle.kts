plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:domain:authentication"))
            }
        }
    }
}
