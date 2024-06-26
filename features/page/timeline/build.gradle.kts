plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:domain:timeline"))
                implementation(project(":features:page:toot"))

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.features.timeline" }
