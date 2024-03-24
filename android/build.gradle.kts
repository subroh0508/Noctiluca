plugins {
    id("android-application")
}

dependencies {
    implementation(project(":core:network:authorization"))
    implementation(project(":core:network:instancessocial"))
    implementation(project(":core:network:mastodon"))
    implementation(project(":core:datastore"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))

    implementation(project(":app-shared"))
    implementation(project(":features:designsystem"))
    implementation(project(":features:shared"))
    implementation(project(":features:navigation"))
    implementation(project(":features:page:accountdetail"))
    implementation(project(":features:page:signin"))
    implementation(project(":features:page:statusdetail"))
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

    implementation(libs.image.loader)

    implementation(libs.voyager.navigator)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.koin)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    testImplementation(libs.junit.core)
}

android { namespace = "app.noctiluca" }
