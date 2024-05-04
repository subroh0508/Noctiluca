plugins {
    id("multiplatform-library")
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.junit.core)
                implementation(libs.androidx.test.junit)
            }
        }
    }
}

android { namespace = "noctiluca.test.ui" }
