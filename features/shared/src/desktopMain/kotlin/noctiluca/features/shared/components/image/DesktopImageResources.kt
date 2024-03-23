package noctiluca.features.shared.components.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
actual fun imageResources(image: String) = painterResource("drawable/$image")
