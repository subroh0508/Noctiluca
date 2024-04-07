package noctiluca.features.attachment.component

import android.content.Context
import android.view.View
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.ControllerVisibilityListener
import noctiluca.model.Uri

@Composable
actual fun VideoPlayer(
    url: Uri,
    isControllerVisible: Boolean,
    onChangeControllerVisibility: (Boolean) -> Unit,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url.value))
            prepare()
            seekTo(0L)
            play()
        }
    }

    AndroidView(
        factory = {
            startPlayerView(
                it,
                exoPlayer,
                isControllerVisible,
                onChangeControllerVisibility = onChangeControllerVisibility,
            )
        },
        modifier = modifier,
    )
}

@OptIn(UnstableApi::class)
private fun startPlayerView(
    context: Context,
    exoPlayer: ExoPlayer,
    isControllerVisible: Boolean,
    onChangeControllerVisibility: (Boolean) -> Unit,
) = PlayerView(context).apply {
    player = exoPlayer
    if (isControllerVisible) {
        showController()
    } else {
        hideController()
    }

    setControllerVisibilityListener(
        ControllerVisibilityListener { visibility ->
            onChangeControllerVisibility(visibility == View.VISIBLE)
        }
    )
}
