pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        includeBuild("plugins")
    }
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
    ":app-shared",
    ":core:network:authorization",
    ":core:network:instancessocial",
    ":core:network:mastodon",
    ":core:datastore",
    ":core:data",
    ":core:test:shared",
    ":core:test:ui",
    ":core:model",
    ":core:domain:timeline",
    ":features:navigation",
    ":features:shared",
    ":features:designsystem",
    ":features:page:accountdetail",
    ":features:page:attachment",
    ":features:page:signin",
    ":features:page:statusdetail",
    ":features:page:timeline",
    ":features:page:toot",
)
