plugins {
    id("features")
    id("org.jetbrains.compose")
    id("has-resources")
    id("test.multiplatform-ui-test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:data"))

                implementation(project(":features:designsystem"))
                implementation(project(":features:navigation"))
                implementation(project(":features:shared"))
            }
        }
    }
}
