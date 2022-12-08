import extension.androidConfig
import extension.proguardConfig
import extension.Target
import extension.targets
import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

kotlin {
    targets(Target.ANDROID, Target.DESKTOP)

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        named("androidMain")
        named("androidTest")
        named("desktopMain") {
            dependencies {
                implementation(compose.preview)
            }
        }
        named("desktopTest")
    }
}

android {
    androidConfig()
    proguardConfig()
}
