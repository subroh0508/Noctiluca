package noctiluca.components.atoms.divider

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material3.Divider as MaterialDivider

@Composable
fun Divider(
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val heightDp = remember(density) { with (density) { 1F.toDp() } }

    MaterialDivider(
        modifier = Modifier.fillMaxWidth()
            .height(heightDp)
            .then(modifier),
    )
}
