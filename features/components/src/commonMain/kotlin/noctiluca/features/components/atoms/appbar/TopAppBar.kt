package noctiluca.features.components.atoms.appbar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val NavigateIconSize = 48.dp

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
) = CenterAlignedTopAppBar(
    { Text(title) },
    modifier,
    navigationIcon,
)
