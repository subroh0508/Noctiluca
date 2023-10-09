plugins {
    id("features")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:data:account:model"))
                implementation(project(":core:data:status:model"))
                implementation(project(":features:components"))

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.features.shared" }
