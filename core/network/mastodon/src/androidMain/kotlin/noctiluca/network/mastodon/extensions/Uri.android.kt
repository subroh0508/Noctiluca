package noctiluca.network.mastodon.extensions

import noctiluca.model.Uri
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.buffer
import java.io.File

internal actual fun Uri.toByteArray() = FileSystem.SYSTEM
    .source(File(value).toOkioPath())
    .buffer()
    .readByteArray()
