package noctiluca.features.components.atoms.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val NumberCircleSize = 40.dp

@Composable
fun NumberCircle(
    n: Int,
    size: Dp = NumberCircleSize,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.size(size)
        .clip(CircleShape)
        .background(backgroundColor)
        .then(modifier)
) {
    Text(
        n.toString(),
        color = color,
        textAlign = TextAlign.Center,
    )
}
