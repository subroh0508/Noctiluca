plugins {
    id("features")
    id("has-resources")
    id("org.jetbrains.compose")
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
