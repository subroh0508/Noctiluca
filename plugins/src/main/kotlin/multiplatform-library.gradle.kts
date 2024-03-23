import extension.*
import extension.Target
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    targets(
        Target.ANDROID,
        Target.IOS,
        Target.DESKTOP,
    )

    sourceSets {
        named("commonMain") {
            dependencies {
                // Workaround: https://github.com/cashapp/sqldelight/issues/4357
                implementation("co.touchlab:stately-common:2.0.5")
            }
        }
    }
}

android {
    androidLibraryConfig()
    proguardLibraryConfig()
}
