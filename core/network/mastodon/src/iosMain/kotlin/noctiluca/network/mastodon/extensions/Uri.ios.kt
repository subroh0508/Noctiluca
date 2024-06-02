package noctiluca.network.mastodon.extensions

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import noctiluca.model.Uri
import noctiluca.model.extensions.readData
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.posix.memcpy

internal actual fun Uri.toByteArray() = NSURL.fileURLWithPath(value)
    .readData()
    .toByteArray()

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray() = ByteArray(length.toInt()).apply {
    usePinned { memcpy(it.addressOf(0), bytes, length) }
}
