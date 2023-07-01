package noctiluca.features.components

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class ViewModel(
    protected val coroutineScope: CoroutineScope,
) {
    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.LAZY,
        block: suspend CoroutineScope.() -> Unit,
    ) = coroutineScope.launch(
        context,
        start,
        block,
    )
}
