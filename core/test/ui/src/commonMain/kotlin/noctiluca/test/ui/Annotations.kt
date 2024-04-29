package noctiluca.test.ui

import kotlin.reflect.KClass

expect abstract class Runner
expect class UiTestRunner : Runner

expect abstract class ParentRunner<T> : Runner
expect class FrameworkMethod
expect open class BlockClassRunner : ParentRunner<FrameworkMethod>
expect open class SandboxTestRunner : BlockClassRunner

expect annotation class RunWith(val value: KClass<out Runner>)
