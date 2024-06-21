package noctiluca.data.status.impl

import noctiluca.data.status.MediaRepository
import noctiluca.data.status.toEntity
import noctiluca.model.media.LocalMediaFile
import noctiluca.network.mastodon.MastodonApiV2

internal class MediaRepositoryImpl(
    private val v2: MastodonApiV2,
) : MediaRepository {
    override suspend fun upload(media: LocalMediaFile) = v2.postMedia(media).toEntity()
}
