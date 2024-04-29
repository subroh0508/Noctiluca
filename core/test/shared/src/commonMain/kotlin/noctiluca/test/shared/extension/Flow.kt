package noctiluca.test.shared.extension

import io.kotest.core.test.TestScope
import io.kotest.core.test.testCoroutineScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
suspend fun <T> TestScope.flowToList(
    flow: Flow<T>,
): List<T> {
    val instances = mutableListOf<T>()

    flow.onEach(instances::add)
        .stateIn(CoroutineScope(UnconfinedTestDispatcher(testCoroutineScheduler)))

    return instances
}
