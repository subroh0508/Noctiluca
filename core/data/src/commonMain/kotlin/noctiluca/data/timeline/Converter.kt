package noctiluca.data.timeline

import noctiluca.model.timeline.Timeline
import noctiluca.network.mastodon.data.streaming.Stream

internal fun Timeline.toStream() = when (this) {
    is Timeline.Global -> buildGlobalStream(onlyRemote, onlyMedia)
    is Timeline.Local -> buildLocalStream(onlyMedia)
    is Timeline.Home -> Stream.USER
    is Timeline.HashTag -> Stream.HASHTAG
    is Timeline.List -> Stream.LIST
}

private fun buildGlobalStream(
    onlyRemote: Boolean,
    onlyMedia: Boolean,
) = when {
    onlyRemote && onlyMedia -> Stream.PUBLIC_REMOTE_MEDIA
    onlyRemote && !onlyMedia -> Stream.PUBLIC_REMOTE
    !onlyRemote && onlyMedia -> Stream.PUBLIC_MEDIA
    else -> Stream.PUBLIC
}

private fun buildLocalStream(
    onlyMedia: Boolean
) = if (onlyMedia) Stream.PUBLIC_LOCAL_MEDIA else Stream.PUBLIC_LOCAL
