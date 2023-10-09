plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:domain:authentication"))
                implementation(project(":features:shared"))
            }
        }
    }
}

android { namespace = "noctiluca.features.authentication" }
