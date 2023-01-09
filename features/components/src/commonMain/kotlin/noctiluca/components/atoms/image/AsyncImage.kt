package noctiluca.components.atoms.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import noctiluca.components.di.getKoinOrNull
import noctiluca.components.utils.ImageLoader
import noctiluca.model.Uri

@Composable
fun AsyncImage(
    uri: Uri?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    fallback: Painter? = null,
) {
    val loader: ImageLoader? = remember { getKoinOrNull()?.get() }
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(uri, fallback) {
        imageBitmap = loader?.loadImage(uri)?.getOrNull()
    }

    LoadedImage(imageBitmap, fallback, contentDescription, modifier)
}

@Composable
private fun LoadedImage(
    imageBitmap: ImageBitmap?,
    fallback: Painter?,
    contentDescription: String?,
    modifier: Modifier,
) = when {
    imageBitmap != null -> Image(imageBitmap, contentDescription, modifier)
    fallback != null -> Image(fallback, contentDescription, modifier)
    else -> Spacer(modifier)
}
