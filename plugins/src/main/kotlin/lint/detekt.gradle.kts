package lint

import extension.detektFormatting
import extension.libs
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.kotlin.dsl.register

plugins {
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    detektPlugins(libs.detektFormatting)
}

tasks.register(TASK_DETEKT_ALL, Detekt::class) {
    description = "Runs the Lint check whole project at once."

    basePath = rootProject.projectDir.absolutePath
    parallel = true
    autoCorrect = true
    setSource(files(rootProject.projectDir))
    config.setFrom(listOf(rootProject.files("config/detekt.yml")))

    include("**/*.kt", "**/**.kts")
    exclude("**/res/**", "**/build/**", "**/libs/**")

    reports {
        html {
            required.set(true)
            outputLocation.set(rootProject.file("lint-reports/kotlin/detekt.html"))
        }

        sarif {
            required.set(true)
            outputLocation.set(rootProject.file("lint-reports/kotlin/detekt.sarif"))
        }

        txt {
            required.set(false)
        }
    }
}
