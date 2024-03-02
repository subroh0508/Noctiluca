package extension

import Packages
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.get

fun LibraryExtension.androidLibraryConfig() {
    androidConfig()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

fun ApplicationExtension.androidApplicationConfig() {
    androidConfig()
    defaultConfig {
        applicationId = "app.noctiluca"
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions.isCoreLibraryDesugaringEnabled = true
}

fun LibraryExtension.proguardLibraryConfig() {
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
            consumerProguardFiles("proguard-rules.pro")
        }
    }
}

fun ApplicationExtension.proguardApplicationConfig() {
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
        }
    }
}

private fun CommonExtension<*, *, *, *, *, *>.androidConfig() {
    defaultConfig {
        compileSdk = Packages.compileSdk
        minSdk = Packages.minSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // @see: https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-debug/
    packagingOptions {
        resources {
            excludes.add("META-INF/**")
            excludes.add("META-INF/licenses/**")
            pickFirsts.add("**/attach_hotspot_windows.dll")
        }
    }
}

fun DependencyHandler.coreLibraryDesugaring(
    dependencyNotation: Any,
) = add("coreLibraryDesugaring", dependencyNotation)
