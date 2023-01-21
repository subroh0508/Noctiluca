import extension.koinAndroid
import extension.koinCore
import extension.libs

plugins {
    id("multiplatform-library")
    id("common-model")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(libs.koinCore)
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.koinAndroid)
            }
        }
        named("androidTest")
        named("desktopMain")
        named("desktopTest")
    }
}
