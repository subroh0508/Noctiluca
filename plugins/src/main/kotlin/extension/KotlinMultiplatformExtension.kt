package extension

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

enum class Target { ANDROID, IOS, DESKTOP }

fun KotlinMultiplatformExtension.targets(vararg target: Target) {
    target.toList().forEach {
        when (it) {
            Target.ANDROID -> androidTarget()
            Target.IOS -> iosTarget()
            Target.DESKTOP -> desktop()
        }
    }
    applyDefaultHierarchyTemplate()
}

private fun KotlinMultiplatformExtension.desktop() {
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
    }
}

private fun KotlinMultiplatformExtension.iosTarget() {
    iosX64()
    iosArm64()
    iosSimulatorArm64()
}
