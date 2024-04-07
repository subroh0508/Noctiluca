package noctiluca.features.shared.status.attachment

import android.content.Context
import android.widget.VideoView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import noctiluca.model.Uri

@Composable
actual fun VideoPlayer(
    url: Uri,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url.value))
            prepare()
            seekTo(0L)
        }
    }

    AndroidView(
        factory = { startPlayerView(it, exoPlayer) },
        modifier = modifier,
    )
}

private fun startPlayerView(
    context: Context,
    exoPlayer: ExoPlayer,
) = PlayerView(context).apply {
    player = exoPlayer
}
