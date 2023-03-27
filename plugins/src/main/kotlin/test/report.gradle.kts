package test

tasks.register(TASK_TEST_DEBUG_UNIT_TEST_REPORT, TestReport::class) {
    destinationDirectory.set(file("$buildDir/reports/$TASK_TEST_DEBUG_UNIT_TEST"))

    subprojects.forEach {
        val test = it.tasks.findByName(TASK_TEST_DEBUG_UNIT_TEST) as? Test
        if (test != null) testResults.from(test.binaryResultsDirectory)
    }
}

tasks.register(TASK_TEST_DESKTOP_TEST_REPORT, TestReport::class) {
    destinationDirectory.set(file("$buildDir/reports/$TASK_TEST_DESKTOP_TEST"))

    subprojects.forEach {
        val test = it.tasks.findByName(TASK_TEST_DESKTOP_TEST) as? Test
        if (test != null) testResults.from(test.binaryResultsDirectory)
    }
}
