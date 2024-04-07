package noctiluca.features.attachment.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.interop.UIKitView
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
    val localViewController = LocalUIViewController.current
    val navigator = LocalNavigator.current

    playerViewController.player = player
    playerViewController.showsPlaybackControls = true
    playerViewController.entersFullScreenWhenPlaybackBegins = true
    playerViewController.exitsFullScreenWhenPlaybackEnds = true

    UIKitView(
        factory = {
            UIView().apply {
                localViewController.presentViewController(
                    playerViewController,
                    animated = true,
                    completion = null,
                )
            }
        },
        update = {
            player.play()
            playerViewController.player?.play()
        },
        onRelease = {
            println("onRelease")
        },
        onResize = { view: UIView, rect: CValue<CGRect> ->
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            view.layer.setFrame(rect)
            playerViewController.view.layer.frame = rect
            CATransaction.commit()
        },
        modifier = modifier,
    )
}
