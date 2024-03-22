package noctiluca.datastore.internal

import java.io.File

internal fun preferenceDataStoreFile(
    dir: File? = null,
    fileName: String,
) = dataStoreFile(
    dir,
    "$fileName.preferences_pb",
)

internal fun dataStoreFile(
    dir: File? = null,
    fileName: String,
) = File(
    dir,
    "datastore/$fileName",
)
