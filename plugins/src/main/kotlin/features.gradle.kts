import extension.*

plugins {
    id("multiplatform-library")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":core:model"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.resources)
                implementation(compose.preview)

                implementation(libs.coroutinesCore)

                implementation(libs.voyagerNavigator)
                implementation(libs.voyagerTransitions)
                implementation(libs.voyagerKoin)

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
                // Workaround for https://stackoverflow.com/q/77341341
                implementation(libs.androidxComposeMaterial3)

                implementation(libs.koinAndroid)
            }
        }
        named("androidUnitTest")
        named("desktopMain")
        named("desktopTest")
    }
}
