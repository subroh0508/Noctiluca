package test

tasks.register(TASK_TEST_DEBUG_UNIT_TEST_REPORT, TestReport::class) {
    destinationDir = file("$buildDir/reports/allTests/$TASK_TEST_DEBUG_UNIT_TEST")

    subprojects.forEach {
        val test = it.tasks.findByName(TASK_TEST_DEBUG_UNIT_TEST) as? Test
        if (test != null) reportOn(test)
    }
}

tasks.register(TASK_TEST_DESKTOP_TEST_REPORT, TestReport::class) {
    destinationDir = file("$buildDir/reports/allTests/$TASK_TEST_DESKTOP_TEST")

    subprojects.forEach {
        val test = it.tasks.findByName(TASK_TEST_DESKTOP_TEST) as? Test
        if (test != null) reportOn(test)
    }
}
