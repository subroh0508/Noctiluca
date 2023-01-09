import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tasks.ResourceGeneratorTask

plugins {
    id("features")
    id("has-resources")
    id("org.jetbrains.compose")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":features:components"))
                implementation(project(":features:theme"))
            }
        }
    }
}
