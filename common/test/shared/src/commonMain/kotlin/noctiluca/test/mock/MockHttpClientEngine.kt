package noctiluca.test.mock

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.serialization.*
import io.ktor.utils.io.*
import noctiluca.test.util.isMatched

object MockHttpClientEngine {
    val format = ResourcesFormat()

    inline operator fun <reified T> invoke(
        resource: T,
        expected: String,
    ) = MockEngine { request ->
        if (!request.url.isMatched(format, resource)) {
            return@MockEngine mockError()
        }

        respond(
            content = ByteReadChannel(expected),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
    }

    inline operator fun <reified T> invoke(
        resource: T,
        errorStatusCode: HttpStatusCode,
    ) = MockEngine { request ->
        if (!request.url.isMatched(format, resource)) {
            return@MockEngine mockError()
        }

        respond(
            content = ByteReadChannel("\"error\":\"${errorStatusCode.description}\""),
            status = errorStatusCode,
            headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )
    }

    inline operator fun <reified T> invoke(
        resource: T,
        crossinline handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData,
    ) = MockEngine { request ->
        if (!request.url.isMatched(format, resource)) {
            return@MockEngine mockError()
        }

        handler(request)
    }

    fun MockRequestHandleScope.mockError() = respond(
        content = ByteReadChannel("{\"error\":\"mock error\"}"),
        status = HttpStatusCode.InternalServerError,
        headers = headersOf(HttpHeaders.ContentType, "application/json"),
    )
}
