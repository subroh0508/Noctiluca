pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        includeBuild("plugins")
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.multiplatform",
                "org.jetbrains.kotlin.android" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                "org.jetbrains.plugin.serialization" -> ("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
                "com.android.application",
                "com.android.library" -> useModule("com.android.tools.build:gradle:${requested.version}")
                "org.jetbrains.compose" -> useModule("org.jetbrains.compose:compose-gradle-plugin:${requested.version}")
            }
        }
    }
    /*
    plugins {
        kotlin("multiplatform") version "1.7.20"
        kotlin("android") version "1.7.20"
        id("com.android.application") version "7.3.0"
        id("com.android.library") version "7.3.0"
        id("org.jetbrains.compose") version "1.2.1"
    }
    */
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "noctiluca"

include(
    ":android",
    ":desktop",
    ":common:data:api:authentication",
    ":common:data:api:mastodon",
    ":common:data:shared",
    ":common:data:account:model",
    ":common:data:status:model",
    ":features:timeline",
)
