group "net.subroh0508"
version "1.0-SNAPSHOT"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id(libs.plugins.kotlin.mpp.get().pluginId) apply false
    id(libs.plugins.kotlin.android.get().pluginId) apply false
    id(libs.plugins.android.application.get().pluginId) apply false
    id(libs.plugins.android.library.get().pluginId) apply false
    id(libs.plugins.compose.get().pluginId) apply false
}
