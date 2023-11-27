package noctiluca.features.shared.atoms.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.rememberImageSuccessPainter
import com.seiko.imageloader.ui.AutoSizeBox
import noctiluca.model.Uri

@Composable
fun AsyncImage(
    uri: Uri?,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    fallback: Painter? = null,
) {
    if (uri != null) {
        AutoSizeBox(
            uri.value,
            contentAlignment = alignment,
        ) { action ->
            when (action) {
                is ImageAction.Success -> Image(
                    rememberImageSuccessPainter(action),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = modifier,
                )

                is ImageAction.Failure -> FallbackImage(
                    fallback,
                    contentScale,
                    contentDescription,
                    modifier,
                )

                is ImageAction.Loading -> Spacer(modifier)
            }
        }
        return
    }

    FallbackImage(
        fallback,
        contentScale,
        contentDescription,
        modifier,
    )
}

@Composable
private fun FallbackImage(
    painter: Painter?,
    contentScale: ContentScale,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) = if (painter != null) {
    Image(
        painter,
        contentDescription,
        modifier,
        contentScale = contentScale,
    )
} else {
    Spacer(modifier)
}
