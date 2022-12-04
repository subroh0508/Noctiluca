package extension

import Packages
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.get

fun CommonExtension<*, *, *, *>.androidConfig() {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        compileSdk = Packages.compileSdk
        minSdk = Packages.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

fun LibraryExtension.proguardConfig() {
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
            consumerProguardFiles("proguard-rules.pro")
        }
    }
}
