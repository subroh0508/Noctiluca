import extension.koinAndroid
import extension.koinCore
import extension.libs

plugins {
    id("multiplatform-library")
    id("test.multiplatform-unit-test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koinCore)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.koinAndroid)
            }
        }
    }
}
