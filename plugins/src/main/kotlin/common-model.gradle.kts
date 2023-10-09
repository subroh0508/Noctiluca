plugins {
    id("multiplatform-library")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":core:data:shared"))
            }
        }
    }
}
