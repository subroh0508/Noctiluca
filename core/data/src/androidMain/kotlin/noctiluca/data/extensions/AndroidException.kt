package noctiluca.data.extensions

internal actual fun handleUnknownHostException(e: Throwable?) {
    if (e is java.net.UnknownHostException) {
        throw UnknownHostException(e)
    }
}
