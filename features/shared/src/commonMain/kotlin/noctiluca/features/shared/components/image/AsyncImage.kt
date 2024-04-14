package noctiluca.features.shared.components.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberImageActionPainter
import com.seiko.imageloader.rememberImageSuccessPainter
import com.seiko.imageloader.ui.AutoSizeBox
import io.ktor.http.URLProtocol
import noctiluca.model.Uri
import okio.Path.Companion.toPath

@Composable
fun AsyncImage(
    uri: Uri?,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    fallback: Painter? = null,
) {
    if (uri == null) {
        FallbackImage(
            fallback,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )
        return
    }

    if (uri.isOnline) {
        NetworkImage(
            uri,
            alignment,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            fallback = fallback,
        )
        return
    }

    AutoSizeBox(
        ImageRequest { data(uri.value.toPath()) },
        contentAlignment = alignment,
    ) {
        Image(
            rememberImageActionPainter(it),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )
    }
}

private val Uri.isOnline
    get() = listOf(
        URLProtocol.HTTP.name,
        URLProtocol.HTTPS.name,
    ).any { value.startsWith(it) }

@Composable
private fun NetworkImage(
    uri: Uri,
    alignment: Alignment,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale,
    fallback: Painter?,
) = AutoSizeBox(
    uri.value,
    contentAlignment = alignment,
) { action ->
    when (action) {
        is ImageAction.Success -> Image(
            rememberImageSuccessPainter(action),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )

        is ImageAction.Failure -> FallbackImage(
            fallback,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )

        is ImageAction.Loading -> Spacer(modifier)
    }
}

@Composable
private fun FallbackImage(
    painter: Painter?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale,
) = if (painter != null) {
    Image(
        painter,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
    )
} else {
    Spacer(modifier)
}
