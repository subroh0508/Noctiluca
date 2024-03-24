package noctiluca.features.shared.components.appbar

import androidx.compose.material3.*
import androidx.compose.ui.unit.dp

val NavigateIconSize = 48.dp

@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarScrollBehavior.scrollToTop() {
    state.heightOffset = 0F
    state.contentOffset = 0F
}
