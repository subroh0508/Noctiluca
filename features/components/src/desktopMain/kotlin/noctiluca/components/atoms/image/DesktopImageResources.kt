package noctiluca.components.atoms.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
actual fun imageResources(image: String) = painterResource("drawables/$image")
