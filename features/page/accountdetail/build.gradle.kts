plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:domain:accountdetail"))
                implementation(project(":features:shared"))
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.navigation.compose)
            }
        }
    }
}

android { namespace = "noctiluca.features.accountdetail" }
