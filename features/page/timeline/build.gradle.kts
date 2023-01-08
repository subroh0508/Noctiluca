plugins {
    id("features-page")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:domain:timeline"))
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
