package noctiluca.features.components.utils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap

internal actual fun ByteArray.toImageBitmap() = BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
