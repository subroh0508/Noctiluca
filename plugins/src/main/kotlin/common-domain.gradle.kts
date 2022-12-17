import extension.koinAndroid
import extension.koinCore
import extension.libs
import gradle.kotlin.dsl.accessors._533be12ecf36a3a2c2d759fae22357cd.kotlin
import gradle.kotlin.dsl.accessors._533be12ecf36a3a2c2d759fae22357cd.sourceSets

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
