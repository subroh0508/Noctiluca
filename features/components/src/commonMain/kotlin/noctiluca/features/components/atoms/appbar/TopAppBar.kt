package noctiluca.features.components.atoms.appbar

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val NavigateIconSize = 48.dp

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlineTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.largeTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) = LargeTopAppBar(
    title,
    modifier,
    {
        IconButton(onClick = onBackPressed) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
            )
        }
    },
    colors = colors,
    scrollBehavior = scrollBehavior,
)

@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarScrollBehavior.scrollToTop() {
    state.heightOffset = 0F
    state.contentOffset = 0F
}
