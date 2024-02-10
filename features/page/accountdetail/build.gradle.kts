plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android { namespace = "noctiluca.features.accountdetail" }
