import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.bundles.plugins)
    implementation(libs.kotlin.poet)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn",
        )
    }
}
