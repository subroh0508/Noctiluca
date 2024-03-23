package noctiluca.data.extensions

internal class UnknownHostException(cause: Exception) : Exception(cause)

internal expect fun handleUnknownHostException(e: Throwable?)
