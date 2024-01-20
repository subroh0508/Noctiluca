plugins {
    id("features")
    id("org.jetbrains.compose")
    id("has-resources")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":features:designsystem"))
                implementation(project(":features:navigation"))
                implementation(project(":features:shared"))
            }
        }
    }
}
