package noctiluca.features.components.atoms.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

val LocalSnackbarHostState = compositionLocalOf { SnackbarHostState() }

@Composable
fun showSnackbar(text: String) {
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()

    scope.launch { snackbarHostState.showSnackbar(text) }
}
