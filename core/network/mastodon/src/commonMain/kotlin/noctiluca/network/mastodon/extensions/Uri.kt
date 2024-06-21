package noctiluca.network.mastodon.extensions

import noctiluca.model.Uri

internal val Uri.filename get() = value.split("/").last()

internal expect fun Uri.toByteArray(): ByteArray
