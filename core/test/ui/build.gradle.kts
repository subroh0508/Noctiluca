plugins {
    id("multiplatform-library")
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(libs.androidx.test.junit)
                implementation(libs.robolectric)
            }
        }
    }
}

android { namespace = "noctiluca.test.ui" }
