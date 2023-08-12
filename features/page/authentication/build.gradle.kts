plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:domain:authentication"))
                implementation(project(":features:shared"))
            }
        }
    }
}

android { namespace = "noctiluca.features.authentication" }
