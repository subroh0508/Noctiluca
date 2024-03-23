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
}

android {
    androidLibraryConfig()
    proguardLibraryConfig()
}
