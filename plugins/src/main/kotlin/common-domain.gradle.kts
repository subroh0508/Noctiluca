import extension.koinAndroid
import extension.koinCore
import extension.libs

plugins {
    id("multiplatform-library")
    id("org.jetbrains.compose")
    id("common-model")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(libs.koinCore)

                implementation(compose.runtime)
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
