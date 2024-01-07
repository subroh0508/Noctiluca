package noctiluca.test.extension

import io.kotest.core.coroutines.backgroundScope
import io.kotest.core.test.TestScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> TestScope.flowToList(
    flow: Flow<T>,
): List<T> {
    val instances = mutableListOf<T>()

    flow.onEach(instances::add)
        .stateIn(backgroundScope)

    return instances
}
