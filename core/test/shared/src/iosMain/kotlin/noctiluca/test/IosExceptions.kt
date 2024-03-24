package noctiluca.test

import io.ktor.client.engine.darwin.DarwinHttpRequestException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorCannotFindHost

actual val MockUnknownHostException: Exception = DarwinHttpRequestException(
    NSError.errorWithDomain(
        domain = null,
        code = NSURLErrorCannotFindHost,
        userInfo = null,
    )
)
