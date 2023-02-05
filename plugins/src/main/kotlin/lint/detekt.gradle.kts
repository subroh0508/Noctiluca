package lint

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.kotlin.dsl.register

plugins {
    id("io.gitlab.arturbosch.detekt")
}

tasks.register("detektAll", Detekt::class) {
    description = "Runs the Lint check whole project at once."

    parallel = true
    // autoCorrect = true
    setSource(files(rootProject.projectDir))
    config.setFrom(listOf(rootProject.files("config/detekt.yml")))

    include("**/*.kt", "**/**.kts")
    exclude("**/res/**", "**/build/**", "**/libs/**")

    reports {
        html {
            required.set(true)
            outputLocation.set(rootProject.file("lint-reports/kotlin/detekt.html"))
        }

        xml {
            required.set(true)
            outputLocation.set(rootProject.file("lint-reports/kotlin/detekt.xml"))
        }

        txt {
            required.set(false)
        }
    }
}
