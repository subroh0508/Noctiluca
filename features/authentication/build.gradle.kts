plugins {
    id("features")
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
