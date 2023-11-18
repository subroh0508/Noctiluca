package noctiluca.features.shared.utils

import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

internal actual fun ByteArray.toImageBitmap() = Image.makeFromEncoded(this).toComposeImageBitmap()
