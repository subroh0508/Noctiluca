package noctiluca.components.atoms.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import noctiluca.components.di.getKoinOrNull
import noctiluca.components.utils.ImageLoader
import noctiluca.model.Uri

@Composable
fun AsyncImage(
    uri: Uri?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    fallbackUri: Uri? = null,
) {
    val loader: ImageLoader? = remember { getKoinOrNull()?.get() }
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(uri, fallbackUri) {
        imageBitmap = loader?.loadImage(uri ?: fallbackUri)?.getOrNull()
    }

    imageBitmap?.let { Image(it, contentDescription, modifier) }
}
