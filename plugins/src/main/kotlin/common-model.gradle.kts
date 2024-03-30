plugins {
    id("multiplatform-library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:data"))
            }
        }
    }
}
