import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import tasks.ResourceGeneratorTask

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir("${layout.buildDirectory.asFile.get().absolutePath}/generated/resources")
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

project.tasks.withType<KotlinCompile> { dependsOn(generateSharedResources) }
project.tasks.withType<KotlinNativeCompile> { dependsOn(generateSharedResources) }
