package noctiluca.features.components.atoms.appbar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val NavigateIconSize = 48.dp

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) = CenterAlignedTopAppBar(
    { Text(title) },
    modifier,
    navigationIcon,
    scrollBehavior = scrollBehavior,
)

fun TopAppBarScrollBehavior.scrollToTop() {
    state.offset = 0F
    state.contentOffset = 0F
}
