package noctiluca.features.attachment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
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

    var isVisible by remember { mutableStateOf(isControllerVisible) }

    val player = remember { AVPlayer(uRL = nsUrl) }
    val playerViewController = remember { AVPlayerViewController() }

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
            modifier = Modifier.fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        isVisible = !isVisible
                        onChangeControllerVisibility(isVisible)
                    },
                )
        )
    }
}
