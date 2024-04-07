package noctiluca.features.attachment.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import noctiluca.model.Uri
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component

// @see: https://github.com/Kashif-E/Compose-Multiplatform-Video-Player/blob/master/common/src/desktopMain/kotlin/com/kashif/common/VideoPlayer.kt
@Composable
actual fun VideoPlayer(
    url: Uri,
    isControllerVisible: Boolean,
    onChangeControllerVisibility: (Boolean) -> Unit,
    modifier: Modifier,
) {
    val mediaPlayerComponent = remember { initializeMediaPlayerComponent() }
    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }

    val factory = remember { { mediaPlayerComponent } }

    LaunchedEffect(url) { mediaPlayer.media().start(url.value) }
    DisposableEffect(Unit) { onDispose(mediaPlayer::release) }

    SwingPanel(
        factory = factory,
        background = MaterialTheme.colorScheme.surface,
        modifier = modifier
    )
}

private fun initializeMediaPlayerComponent(): Component {
    NativeDiscovery().discover()

    return if (isMacOS()) {
        CallbackMediaPlayerComponent()
    } else {
        EmbeddedMediaPlayerComponent()
    }
}

private fun Component.mediaPlayer() = when (this) {
    is CallbackMediaPlayerComponent -> mediaPlayer()
    is EmbeddedMediaPlayerComponent -> mediaPlayer()
    else -> error("mediaPlayer() can only be called on vlcj player components")
}

private fun isMacOS(): Boolean {
    val os = System.getProperty("os.name", "generic")
        .lowercase()

    return "mac" in os || "darwin" in os
}
