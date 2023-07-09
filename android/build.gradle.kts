plugins {
    id("android-application")
}

dependencies {
    implementation(project(":common:api:authentication"))
    implementation(project(":common:api:instancessocial"))
    implementation(project(":common:api:mastodon"))
    implementation(project(":common:api:token"))
    implementation(project(":common:data:shared"))
    implementation(project(":common:data:account:infra"))
    implementation(project(":common:data:accountdetail:infra"))
    implementation(project(":common:data:authentication:infra"))
    implementation(project(":common:data:instance:infra"))
    implementation(project(":common:data:timeline:infra"))
    implementation(project(":common:data:status:infra"))

    implementation(project(":features:theme"))
    implementation(project(":features:components"))
    implementation(project(":features:page:accountdetail"))
    implementation(project(":features:page:authentication"))
    implementation(project(":features:page:timeline"))

    implementation(compose.runtime)
    implementation(compose.foundation)
    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity.compose)

    implementation(libs.bundles.ktor.client.jvm)
    implementation(libs.kotlinx.serialization)

    implementation(libs.decompose.core)
    implementation(libs.decompose.compose.jetbrains)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    testImplementation(libs.junit.core)
}

android { namespace = "app.noctiluca" }
