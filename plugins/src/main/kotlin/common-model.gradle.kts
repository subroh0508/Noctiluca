plugins {
    id("multiplatform-library")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":common:data:shared"))
            }
        }
    }
}
