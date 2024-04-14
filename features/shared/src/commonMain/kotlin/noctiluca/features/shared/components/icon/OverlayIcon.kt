package noctiluca.features.shared.components.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Suppress("MagicNumber")
@Composable
fun OverlayIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
) = Box(
    modifier = Modifier.clickable { onClick() }
        .offset(x = 4.dp, y = 4.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(Color.Black.copy(0.6F))
        .padding(4.dp),
) {
    Icon(
        imageVector,
        contentDescription = contentDescription,
        modifier = Modifier.align(Alignment.Center),
    )
}
