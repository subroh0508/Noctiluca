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
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
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

                implementation(libs.koinAndroid)
            }
        }
        named("androidUnitTest")
        named("desktopMain")
        named("desktopTest")
    }
}
