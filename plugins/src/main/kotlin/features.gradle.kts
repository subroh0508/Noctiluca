import extension.androidxActivities
import extension.koinAndroid
import extension.koinCore
import extension.libs

plugins {
    id("multiplatform-library")
    id("org.jetbrains.compose")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":common:data:shared"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)

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
                implementation(libs.androidxActivities)

                implementation(libs.koinAndroid)
            }
        }
        named("androidTest")
        named("desktopMain") {
            dependencies {
                implementation(compose.preview)
            }
        }
        named("desktopTest")
    }
}
