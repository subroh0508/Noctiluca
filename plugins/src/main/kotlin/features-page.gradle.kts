plugins {
    id("features")
    id("org.jetbrains.compose")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":features:components"))
            }
        }
    }
}
