import extension.*

plugins {
    id("multiplatform-library")
    id("common-model")
    id("test.multiplatform-unit-test")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(libs.koinCore)
            }
        }
        named("commonTest") {
            dependencies {
                implementation(project(":core:network:mastodon"))
                implementation(project(":core:datastore"))
                implementation(project(":core:data"))

                implementation(libs.ktorSerializationKotlinxJson)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.koinAndroid)
            }
        }
        named("desktopMain")
    }
}
