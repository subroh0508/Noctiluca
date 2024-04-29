package noctiluca.test.ui

import kotlin.reflect.KClass

actual abstract class Runner
actual class UiTestRunner : Runner()

actual annotation class RunWith(actual val value: KClass<out Runner>)
