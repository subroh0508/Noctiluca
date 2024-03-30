import extension.*

plugins {
    id("multiplatform-library")
    id("common-model")
    id("test.multiplatform-unit-test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koinCore)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":core:network:mastodon"))
                implementation(project(":core:datastore"))
                implementation(project(":core:data"))

                implementation(libs.ktorSerializationKotlinxJson)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.koinAndroid)
            }
        }
    }
}
