package noctiluca.features.shared.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.ViewModel as AndroidViewModel

actual typealias ViewModel = AndroidViewModel

actual val ViewModel.viewModelScope: CoroutineScope
    get() = this.viewModelScope
