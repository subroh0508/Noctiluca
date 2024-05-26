import io.gitlab.arturbosch.detekt.Detekt
import lint.TASK_DETEKT_ALL
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import test.TASK_TEST_DEBUG_UNIT_TEST
import test.TASK_TEST_DEBUG_UNIT_TEST_REPORT
import test.TASK_TEST_DESKTOP_TEST
import test.TASK_TEST_DESKTOP_TEST_REPORT
import test.TASK_TEST_IOS_SIMULATOR_ARM64_TEST
import test.TASK_TEST_IOS_TEST
import test.TASK_TEST_IOS_TEST_REPORT
import test.TASK_TEST_IOS_X64_TEST

plugins {
    alias(libs.plugins.kotlin.mpp) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.buildconfig) apply false

    // Workaround: https://youtrack.jetbrains.com/issue/KT-67968
    id("multiplatform-library") apply false
    id("test.multiplatform-unit-test") apply false
    id("test.multiplatform-ui-test") apply false
    id("android-application") apply false
    id("common-data") apply false
    id("common-domain") apply false
    id("common-model") apply false
    id("features") apply false
    id("features-page") apply false
    id("has-resources") apply false

    id("lint.detekt") apply false
    id("test.report") apply false
}

// workaround: https://youtrack.jetbrains.com/issue/KT-55947/Unable-to-set-kapt-jvm-target-version
subprojects {
    tasks.withType(KotlinCompile::class) {
        kotlinOptions.jvmTarget = "17"
    }
}

/*  */
tasks.register(TASK_TEST_DEBUG_UNIT_TEST_REPORT, TestReport::class) {
    config<Test>(
        TASK_TEST_DEBUG_UNIT_TEST
    )
}
tasks.register(TASK_TEST_DESKTOP_TEST_REPORT, TestReport::class) {
    config<Test>(
        TASK_TEST_DESKTOP_TEST
    )
}
tasks.register(TASK_TEST_IOS_TEST_REPORT, TestReport::class) {
    config<KotlinNativeSimulatorTest>(
        if (System.getenv("CI") != null) {
            TASK_TEST_IOS_X64_TEST
        } else {
            TASK_TEST_IOS_SIMULATOR_ARM64_TEST
        },
        TASK_TEST_IOS_TEST,
    )
}

@Suppress("UNCHECKED_CAST")
fun <T : AbstractTestTask> TestReport.config(name: String, directoryName: String = name) {
    destinationDirectory.set(layout.buildDirectory.file("reports/$directoryName").get().asFile)

    subprojects.forEach {
        val test = it.tasks.findByName(name) as? T
        if (test != null) testResults.from(test.binaryResultsDirectory)
    }
}
/*  */
