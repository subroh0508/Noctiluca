package noctiluca.data.status

import noctiluca.model.media.LocalMediaFile
import noctiluca.model.status.Attachment

interface MediaRepository {
    suspend fun upload(media: LocalMediaFile): Attachment
}
