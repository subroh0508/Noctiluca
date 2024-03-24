package test

tasks.register(TASK_TEST_DEBUG_UNIT_TEST_REPORT, TestReport::class) {
    config(
        TASK_TEST_DEBUG_UNIT_TEST
    )
}
tasks.register(TASK_TEST_DESKTOP_TEST_REPORT, TestReport::class) { config(TASK_TEST_DESKTOP_TEST) }
tasks.register(TASK_TEST_IOS_TEST_REPORT, TestReport::class) { config(TASK_TEST_IOS_TEST) }

fun TestReport.config(name: String) {
    destinationDirectory.set(file("${layout.buildDirectory}/reports/$name"))

    subprojects.forEach {
        val test = it.tasks.findByName(name) as? Test
        if (test != null) testResults.from(test.binaryResultsDirectory)
    }
}
