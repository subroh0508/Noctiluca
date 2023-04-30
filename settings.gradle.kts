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
    ":common:api:authentication",
    ":common:api:instancessocial",
    ":common:api:mastodon",
    ":common:api:token",
    ":common:data:shared",
    ":common:data:account:model",
    ":common:data:account:infra",
    ":common:data:accountdetail:model",
    ":common:data:accountdetail:infra",
    ":common:data:authentication:model",
    ":common:data:authentication:infra",
    ":common:data:instance:model",
    ":common:data:instance:infra",
    ":common:data:status:model",
    ":common:data:status:infra",
    ":common:data:timeline:model",
    ":common:data:timeline:infra",
    ":common:test:shared",
    ":common:domain:accountdetail",
    ":common:domain:authentication",
    ":common:domain:timeline",
    ":features:components",
    ":features:shared",
    ":features:theme",
    ":features:page:accountdetail",
    ":features:page:authentication",
    ":features:page:timeline",
)
