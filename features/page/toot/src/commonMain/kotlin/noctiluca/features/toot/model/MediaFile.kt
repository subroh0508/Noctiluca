package noctiluca.features.toot.model

import noctiluca.model.Uri
import noctiluca.model.media.LocalMediaFile

internal const val MEDIA_FILE_MAX_SELECTION_SIZE = 4

internal expect val LocalMediaFile.preview: Uri?
