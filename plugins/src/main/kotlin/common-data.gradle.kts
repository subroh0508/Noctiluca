import extension.koinAndroid
import extension.koinCore
import extension.libs

plugins {
    id("multiplatform-library")
    id("test.multiplatform-unit-test")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(libs.koinCore)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.koinAndroid)
            }
        }
        named("desktopMain")
    }
}
