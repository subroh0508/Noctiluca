plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:datastore"))
                implementation(project(":core:data"))
                implementation(project(":core:domain:timeline"))
                implementation(project(":features:shared"))

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.features.timeline" }
