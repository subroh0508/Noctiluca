package test

import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest

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
