import extension.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tasks.ResourceGeneratorTask

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
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.preview)

                implementation(libs.coroutinesCore)

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
        named("desktopMain")
        named("desktopTest")
    }
}
