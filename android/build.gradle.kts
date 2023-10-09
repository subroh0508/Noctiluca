plugins {
    id("android-application")
}

dependencies {
    implementation(project(":core:network:authentication"))
    implementation(project(":core:network:instancessocial"))
    implementation(project(":core:network:mastodon"))
    implementation(project(":core:datastore"))
    implementation(project(":core:data"))

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
