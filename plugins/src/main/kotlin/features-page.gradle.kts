plugins {
    id("features")
    id("has-resources")
    id("org.jetbrains.compose")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":features:components"))
                implementation(project(":features:navigation"))
                implementation(project(":features:theme"))
            }
        }
    }
}
