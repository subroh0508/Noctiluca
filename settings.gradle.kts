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
    ":core:network:authentication",
    ":core:network:instancessocial",
    ":core:network:mastodon",
    ":core:datastore",
    ":core:data",
    ":core:test:shared",
    ":core:model",
    ":core:domain:accountdetail",
    ":core:domain:authentication",
    ":core:domain:timeline",
    ":features:navigation",
    ":features:components",
    ":features:shared",
    ":features:theme",
    ":features:page:accountdetail",
    ":features:page:authentication",
    ":features:page:timeline",
)
