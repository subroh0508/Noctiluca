import extension.*

plugins {
    id("multiplatform-library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-parcelize")
    id("test.multiplatform-unit-test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:model"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.coroutinesCore)

                implementation(libs.voyagerNavigator)
                implementation(libs.voyagerTransitions)
                implementation(libs.voyagerKoin)

                implementation(libs.koinCore)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidxActivities)
                implementation(libs.koinAndroid)
            }
        }
    }
}
