import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tasks.ResourceGeneratorTask

plugins {
    id("features")
    id("org.jetbrains.compose")
}

kotlin {
    sourceSets {
        named("commonMain") {
            kotlin.srcDir("${buildDir.absolutePath}/generated/resources")

            dependencies {
                implementation(project(":features:components"))
                implementation(project(":features:theme"))
            }
        }
    }
}

android {
    sourceSets["main"].res.srcDirs("src/androidMain/res", "src/commonMain/resources")
}

val generateSharedResources by project.tasks.registering(ResourceGeneratorTask::class) {
    group = "build"
    description = "Task for generating resources from commonMain/resources"
}

project.tasks.withType<KotlinCompile> {
    dependsOn(generateSharedResources)
}
