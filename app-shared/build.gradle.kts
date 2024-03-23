plugins {
    id("multiplatform-library")
    id("org.jetbrains.compose")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":core:network:authorization"))
                implementation(project(":core:network:instancessocial"))
                implementation(project(":core:network:mastodon"))
                implementation(project(":core:datastore"))
                implementation(project(":core:data"))
                implementation(project(":core:model"))

                implementation(project(":features:designsystem"))
                implementation(project(":features:shared"))
                implementation(project(":features:navigation"))
                implementation(project(":features:page:accountdetail"))
                implementation(project(":features:page:signin"))
                implementation(project(":features:page:statusdetail"))
                implementation(project(":features:page:timeline"))

                implementation(compose.runtime)
                implementation(compose.foundation)

                implementation(libs.bundles.ktor.client.common)
                implementation(libs.kotlinx.serialization)

                implementation(libs.image.loader)

                implementation(libs.voyager.navigator)

                implementation(libs.koin.core)
            }
        }
    }
}

android { namespace = "app.noctiluca.shared" }
