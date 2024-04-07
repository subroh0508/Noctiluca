package noctiluca.features.attachment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import noctiluca.model.Uri
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.play
import platform.AVKit.AVPlayerViewController
import platform.CoreGraphics.CGRect
import platform.Foundation.NSURL
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayer(
    url: Uri,
    isControllerVisible: Boolean,
    onChangeControllerVisibility: (Boolean) -> Unit,
    modifier: Modifier,
) {
    val nsUrl = NSURL.URLWithString(url.value) ?: return

    val player = remember { AVPlayer(uRL = nsUrl) }
    val playerViewController = remember { AVPlayerViewController() }
    val navigator = LocalNavigator.current

    playerViewController.player = player
    playerViewController.showsPlaybackControls = false

    Box {
        UIKitView(
            factory = {
                playerViewController.view
            },
            update = {
                player.play()
            },
            onResize = { view: UIView, rect: CValue<CGRect> ->
                CATransaction.begin()
                CATransaction.setValue(true, kCATransactionDisableActions)
                view.layer.frame = rect
                CATransaction.commit()
            },
            background = MaterialTheme.colorScheme.surface,
            modifier = modifier,
        )

        Box(
            modifier = Modifier.clickable { navigator?.pop() }
                .offset(x = 16.dp, y = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(0.6F))
                .padding(4.dp),
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
