package noctiluca.network.mastodon

import noctiluca.model.media.LocalMediaFile
import noctiluca.network.mastodon.data.instance.NetworkV2Instance
import noctiluca.network.mastodon.data.mediaattachment.NetworkMediaAttachment

interface MastodonApiV2 {
    suspend fun getInstance(
        domain: String,
    ): NetworkV2Instance

    suspend fun postMedia(
        media: LocalMediaFile,
    ): NetworkMediaAttachment
}
