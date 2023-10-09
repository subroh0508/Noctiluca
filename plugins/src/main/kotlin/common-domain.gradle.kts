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
                implementation(project(":core:api:mastodon"))
                implementation(project(":core:api:token"))
                implementation(project(":core:data:shared"))
                implementation(project(":core:test:shared"))

                implementation(kotlin("test"))
                implementation(libs.ktorSerializationKotlinxJson)
                implementation(libs.ktorClientResources)
                implementation(libs.ktorClientMock)
                implementation(libs.kotestAssertionsCore)
                implementation(libs.kotestFrameworkEngine)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.koinAndroid)
            }
        }
        named("androidUnitTest") {
            dependencies {
                implementation(libs.kotestRunnerJunit5)
            }
        }
        named("desktopMain")
        named("desktopTest") {
            dependencies {
                implementation(libs.kotestRunnerJunit5)
            }
        }
    }
}
