package noctiluca.features.shared.atoms.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.ui.AutoSizeImage
import noctiluca.model.Uri

@Composable
fun AsyncImage(
    uri: Uri?,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    fallback: Painter? = null,
) = when {
    uri != null -> AutoSizeImage(
        uri.value,
        alignment = alignment,
        contentScale = contentScale,
        modifier = modifier,
        contentDescription = contentDescription,
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
