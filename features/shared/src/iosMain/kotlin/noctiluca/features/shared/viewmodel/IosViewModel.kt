package noctiluca.features.shared.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

actual abstract class ViewModel {
    internal val viewModelScope: CoroutineScope = MainScope()
}

actual val ViewModel.viewModelScope: CoroutineScope
    get() = viewModelScope
