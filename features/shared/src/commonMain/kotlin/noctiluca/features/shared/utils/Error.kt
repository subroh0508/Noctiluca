package noctiluca.features.shared.utils

import androidx.compose.runtime.Composable
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import noctiluca.features.shared.getCommonString

fun Throwable.label() = when (this) {
    is ResponseException -> "${response.status.value}: ${response.status.description}"
    else -> this::class.simpleName ?: "UnknownException"
}

@Suppress("MagicNumber")
@Composable
fun Throwable.description(): String {
    if (this is SocketTimeoutException) {
        return getCommonString().error_timeout
    }

    if (this !is ResponseException) {
        return getCommonString().error_unknown
    }

    return when (response.status.value / 100) {
        4 -> getCommonString().error_4xx
        5 -> getCommonString().error_5xx
        else -> getCommonString().error_unknown
    }
}
