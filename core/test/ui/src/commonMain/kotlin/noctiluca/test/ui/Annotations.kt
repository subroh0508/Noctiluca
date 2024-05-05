package noctiluca.test.ui

import kotlin.reflect.KClass

expect abstract class Runner
expect class UiTestRunner : Runner

expect annotation class RunWith(val value: KClass<out Runner>)

expect annotation class ComposeTest()
expect annotation class ComposeIgnore()
