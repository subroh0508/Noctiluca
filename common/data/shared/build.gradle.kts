plugins {
    id("multiplatform-library")
}

kotlin {
    sourceSets {
        commonMain
        androidMain
        desktopMain
    }
}
