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
        commonMain {
            dependencies {
                // Workaround: https://github.com/cashapp/sqldelight/issues/4357
                implementation("co.touchlab:stately-common:2.1.0")
            }
        }
    }
}

android {
    androidLibraryConfig()
    proguardLibraryConfig()
}
