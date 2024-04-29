package noctiluca.test.ui

import kotlin.reflect.KClass

actual abstract class Runner
actual class UiTestRunner : Runner()

actual abstract class ParentRunner<T> : Runner()
actual class FrameworkMethod
actual open class BlockClassRunner : ParentRunner<FrameworkMethod>()
actual open class SandboxTestRunner : BlockClassRunner()

actual annotation class RunWith(actual val value: KClass<out Runner>)
