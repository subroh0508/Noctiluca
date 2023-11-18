package noctiluca.features.shared.atoms.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import noctiluca.features.shared.utils.ImageLoader
import noctiluca.model.Uri
import org.koin.core.context.GlobalContext

private fun getKoinOrNull() = GlobalContext.getOrNull()

@Composable
fun AsyncImage(
    uri: Uri?,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    fallback: Painter? = null,
) {
    val loader: ImageLoader? = remember { getKoinOrNull()?.get() }
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(uri, fallback) {
        imageBitmap = loader?.loadImage(uri)?.getOrNull()
    }

    LoadedImage(
        imageBitmap,
        fallback,
        alignment,
        contentScale,
        contentDescription,
        modifier,
    )
}

@Composable
private fun LoadedImage(
    imageBitmap: ImageBitmap?,
    fallback: Painter?,
    alignment: Alignment,
    contentScale: ContentScale,
    contentDescription: String?,
    modifier: Modifier,
) = when {
    imageBitmap != null -> Image(
        imageBitmap,
        contentDescription,
        modifier,
        alignment = alignment,
        contentScale = contentScale,
    )

    fallback != null -> Image(
        fallback,
        contentDescription,
        modifier,
        alignment = alignment,
        contentScale = contentScale,
    )

    else -> Spacer(modifier)
}
