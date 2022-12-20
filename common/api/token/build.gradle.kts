plugins {
    id("multiplatform-library")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:data:shared"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization)

                implementation(libs.koin.core)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.datastore.core)
                implementation(libs.androidx.datastore.preferences)

                implementation(libs.koin.android)
            }
        }
    }
}
