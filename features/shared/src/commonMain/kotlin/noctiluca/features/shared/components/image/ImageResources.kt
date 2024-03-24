package noctiluca.features.shared.components.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
expect fun imageResources(image: String): Painter
