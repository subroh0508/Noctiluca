plugins {
    id("common-model")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:data:status:model"))

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
