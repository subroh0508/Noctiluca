plugins {
    id("android-application")
}

dependencies {
    implementation(project(":core:api:authentication"))
    implementation(project(":core:api:instancessocial"))
    implementation(project(":core:api:mastodon"))
    implementation(project(":core:api:token"))
    implementation(project(":core:data:shared"))
    implementation(project(":core:data:account:infra"))
    implementation(project(":core:data:accountdetail:infra"))
    implementation(project(":core:data:authentication:infra"))
    implementation(project(":core:data:instance:infra"))
    implementation(project(":core:data:timeline:infra"))
    implementation(project(":core:data:status:infra"))

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
