package noctiluca.data.extensions

import io.ktor.client.engine.darwin.DarwinHttpRequestException
import platform.Foundation.NSURLErrorCannotFindHost

internal actual fun handleUnknownHostException(e: Throwable?) {
    if (e is DarwinHttpRequestException && e.origin.code == NSURLErrorCannotFindHost) {
        throw UnknownHostException(e)
    }
}
