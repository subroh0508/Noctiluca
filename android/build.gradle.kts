plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

dependencies {
    implementation(project(":common:api:authentication"))
    implementation(project(":common:api:instancessocial"))
    implementation(project(":common:api:mastodon"))
    implementation(project(":common:api:token"))
    implementation(project(":common:data:account:infra"))
    implementation(project(":common:data:authentication:infra"))
    implementation(project(":common:data:instance:infra"))
    implementation(project(":common:data:timeline:infra"))

    implementation(project(":features:theme"))
    implementation(project(":features:components"))
    implementation(project(":features:page:authentication"))
    implementation(project(":features:page:timeline"))

    implementation(compose.runtime)
    implementation(compose.foundation)
    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.bundles.ktor.client.jvm)
    implementation(libs.kotlinx.serialization)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    testImplementation(libs.junit)
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "app.noctiluca"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
