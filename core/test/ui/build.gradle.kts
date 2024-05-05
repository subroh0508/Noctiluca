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

        named("desktopMain") {
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }
    }
}

android { namespace = "noctiluca.test.ui" }
