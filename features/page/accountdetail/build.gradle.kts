plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:data"))
                implementation(project(":core:domain:accountdetail"))

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.features.accountdetail" }
