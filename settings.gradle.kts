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
                "com.github.gmazzo.buildconfig" -> useModule("com.github.gmazzo:gradle-buildconfig-plugin:${requested.version}")
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
    ":common:api:authentication",
    ":common:api:instancessocial",
    ":common:api:mastodon",
    ":common:api:token",
    ":common:data:shared",
    ":common:data:account:model",
    ":common:data:account:infra",
    ":common:data:authentication:model",
    ":common:data:authentication:infra",
    ":common:data:instance:model",
    ":common:data:instance:infra",
    ":common:data:status:model",
    ":common:data:status:infra",
    ":common:data:timeline:model",
    ":common:data:timeline:infra",
    ":common:domain:authentication",
    ":common:domain:timeline",
    ":features:components",
    ":features:shared",
    ":features:theme",
    ":features:page:timeline",
    ":features:page:authentication",
)
