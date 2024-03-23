package noctiluca.features.shared.components.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@Composable
actual fun imageResources(image: String): Painter {
    val context = LocalContext.current
    val resName = image.substringBefore(".")

    return painterResource(context.resources.getIdentifier(resName, "drawable", context.packageName))
}
