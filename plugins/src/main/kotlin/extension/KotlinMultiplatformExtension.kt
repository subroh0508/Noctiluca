package extension

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

enum class Target { ANDROID, DESKTOP }

fun KotlinMultiplatformExtension.targets(vararg target: Target) {
    target.toList().forEach {
        when (it) {
            Target.ANDROID -> android()
            Target.DESKTOP -> desktop()
        }
    }
}

private fun KotlinMultiplatformExtension.desktop() {
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
    }
}
