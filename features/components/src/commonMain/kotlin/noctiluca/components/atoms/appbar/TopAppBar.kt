package noctiluca.components.atoms.appbar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
) = CenterAlignedTopAppBar(
    { Text(title) },
    modifier,
)
