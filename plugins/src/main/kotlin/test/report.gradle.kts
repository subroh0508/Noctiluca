package test

tasks.register(TASK_TEST_DEBUG_UNIT_TEST_REPORT, TestReport::class) {
    destinationDir = file("$buildDir/reports/$TASK_TEST_DEBUG_UNIT_TEST")

    subprojects.forEach {
        val test = it.tasks.findByName(TASK_TEST_DEBUG_UNIT_TEST) as? Test
        if (test != null) reportOn(test.binaryResultsDirectory.asFile.get())
    }
}

tasks.register(TASK_TEST_DESKTOP_TEST_REPORT, TestReport::class) {
    destinationDir = file("$buildDir/reports/$TASK_TEST_DESKTOP_TEST")

    subprojects.forEach {
        val test = it.tasks.findByName(TASK_TEST_DESKTOP_TEST) as? Test
        if (test != null) reportOn(test.binaryResultsDirectory.asFile.get())
    }
}
