package noctiluca.features.toot.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.provider.DocumentsContractCompat
import noctiluca.model.Uri
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

// @see: https://gist.github.com/walkingError/915c73ae48882072dc0e8467a813046f
internal fun android.net.Uri.toKmpUri(
    context: Context,
): Uri {
    val absolutePath = when {
        DocumentsContractCompat.isDocumentUri(context, this) -> fromDocumentProvider(context, this)
        scheme == ContentResolver.SCHEME_CONTENT -> getDataColumn(context, this, null, null)
        scheme == ContentResolver.SCHEME_FILE -> path
        else -> null
    }

    absolutePath ?: error("Unknown file path: $path")

    return Uri(absolutePath)
}

private const val AUTHORITY_EXTERNAL_STORAGE_DOCUMENT = "com.android.externalstorage.documents"
private const val AUTHORITY_DOWNLOADS_DOCUMENT = "com.android.providers.downloads.documents"
private const val AUTHORITY_MEDIA_DOCUMENT = "com.android.providers.media.documents"
private const val AUTHORITY_GOOGLE_DRIVE = "com.google.android.apps.docs.storage"

private fun fromDocumentProvider(
    context: Context,
    uri: android.net.Uri,
) = when (uri.authority) {
    AUTHORITY_EXTERNAL_STORAGE_DOCUMENT -> fromExternalStorageDocument(uri)
    AUTHORITY_DOWNLOADS_DOCUMENT -> fromDownloadsDocument(context, uri)
    AUTHORITY_MEDIA_DOCUMENT -> fromMediaDocument(context, uri)
    AUTHORITY_GOOGLE_DRIVE -> fromGoogleDrive(context, uri)
    else -> null
}

private const val VOLUME_PRIMARY = "primary"

private fun fromExternalStorageDocument(
    uri: android.net.Uri,
): String? {
    val (volume, id) = DocumentsContract.getDocumentId(uri).split(":")

    return if (VOLUME_PRIMARY == volume) {
        "${Environment.getExternalStorageDirectory()}/$id"
    } else {
        // handle non-primary volumes
        null
    }
}

private const val PATH_PUBLIC_DOWNLOADS = "content://downloads/public_downloads"

private fun fromDownloadsDocument(
    context: Context,
    uri: android.net.Uri,
): String? {
    val docId = DocumentsContract.getDocumentId(uri)
    val contentUri = ContentUris.withAppendedId(
        android.net.Uri.parse(PATH_PUBLIC_DOWNLOADS),
        docId.toLong(),
    )

    return getDataColumn(context, contentUri, null, null)
}

private const val MEDIA_TYPE_IMAGE = "image"
private const val MEDIA_TYPE_VIDEO = "video"
private const val MEDIA_TYPE_AUDIO = "audio"
private const val QUERY_SELECTION_ID = "_id=?"

private fun fromMediaDocument(
    context: Context,
    uri: android.net.Uri,
): String? {
    val (mediaType, id) = DocumentsContract.getDocumentId(uri).split(":")

    val contentUri = when (mediaType) {
        MEDIA_TYPE_IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        MEDIA_TYPE_VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        MEDIA_TYPE_AUDIO -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        else -> return null
    }

    return getDataColumn(context, contentUri, QUERY_SELECTION_ID, arrayOf(id))
}

private fun fromGoogleDrive(
    context: Context,
    uri: android.net.Uri,
) = saveFromCloudStorage(context, uri)

private const val QUERY_COLUMN_IMAGES_MEDIA_DATA = MediaStore.Images.Media.DATA

private fun getDataColumn(
    context: Context,
    uri: android.net.Uri,
    selection: String?,
    selectionArgs: Array<String>?,
): String? {
    var cursor: Cursor? = null

    val path = runCatching {
        cursor = context.contentResolver.query(
            uri,
            arrayOf(QUERY_COLUMN_IMAGES_MEDIA_DATA),
            selection,
            selectionArgs,
            null,
        )

        cursor?.takeIf { it.moveToFirst() }?.let { c ->
            val index = c.getColumnIndexOrThrow(QUERY_COLUMN_IMAGES_MEDIA_DATA)
            c.getString(index)
        }
    }.getOrNull()

    cursor?.close()
    return path
}

private fun saveFromCloudStorage(
    context: Context,
    uri: android.net.Uri,
): String? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val fileName = getFileName(context, uri) ?: return null
    val originalSize = inputStream.available()

    val targetFile = File(Environment.getExternalStorageDirectory().absolutePath, fileName)

    val bufferedInputStream = BufferedInputStream(inputStream)
    val bufferedOutputStream = BufferedOutputStream(FileOutputStream(targetFile, false))
    val buffer = ByteArray(originalSize)
    bufferedInputStream.read(buffer)

    do {
        bufferedOutputStream.write(buffer)
    } while (bufferedInputStream.read(buffer) != -1)

    bufferedOutputStream.flush()
    bufferedOutputStream.close()
    bufferedInputStream.close()

    return targetFile.absolutePath
}

private fun getFileName(
    context: Context,
    uri: android.net.Uri,
): String? {
    if (uri.scheme != ContentResolver.SCHEME_CONTENT) {
        return uri.path?.split("/")?.lastOrNull()
    }

    var cursor: Cursor? = null

    val path = runCatching {
        cursor = context.contentResolver.query(
            uri,
            null,
            null,
            null,
            null,
        )

        cursor?.takeIf { it.moveToFirst() }?.let { c ->
            val index = c.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
            c.getString(index)
        }
    }.getOrNull()

    cursor?.close()

    return path
}
