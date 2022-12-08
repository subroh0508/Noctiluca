import org.jetbrains.compose.compose

plugins {
    id("common-model")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
