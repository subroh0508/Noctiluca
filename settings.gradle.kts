pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform") version "1.7.20"
        kotlin("android") version "1.7.20"
        id("com.android.application") version "7.3.0"
        id("com.android.library") version "7.3.0"
        id("org.jetbrains.compose") version "1.2.1"
    }
}

rootProject.name = "noctiluca"

include(":android", ":desktop", ":common")
