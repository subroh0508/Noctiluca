plugins {
    id("features")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:data:account:model"))
                implementation(project(":common:data:status:model"))
                implementation(project(":features:components"))

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
