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
    // ":core:network:instancessocial",
    // ":core:network:mastodon",
    // ":core:localcache:token",
    ":core:api:instancessocial",
    ":core:api:mastodon",
    ":core:api:token",
    ":core:data:shared",
    ":core:data:account:model",
    ":core:data:account:infra",
    ":core:data:accountdetail:model",
    ":core:data:accountdetail:infra",
    ":core:data:authentication:model",
    ":core:data:authentication:infra",
    ":core:data:instance:model",
    ":core:data:instance:infra",
    ":core:data:status:model",
    ":core:data:status:infra",
    ":core:data:timeline:model",
    ":core:data:timeline:infra",
    ":core:test:shared",
    ":core:domain:accountdetail",
    ":core:domain:authentication",
    ":core:domain:timeline",
    ":features:components",
    ":features:shared",
    ":features:theme",
    ":features:page:accountdetail",
    ":features:page:authentication",
    ":features:page:timeline",
)
