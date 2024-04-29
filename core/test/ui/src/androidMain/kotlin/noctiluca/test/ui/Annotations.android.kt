package noctiluca.test.ui

actual typealias Runner = org.junit.runner.Runner
actual typealias UiTestRunner = androidx.test.ext.junit.runners.AndroidJUnit4

actual typealias ParentRunner<T> = org.junit.runners.ParentRunner<T>
actual typealias FrameworkMethod = org.junit.runners.model.FrameworkMethod
actual typealias BlockClassRunner = org.junit.runners.BlockJUnit4ClassRunner
actual typealias SandboxTestRunner = org.robolectric.internal.SandboxTestRunner

actual typealias RunWith = org.junit.runner.RunWith
