package noctiluca.test.mock

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.utils.io.*
import noctiluca.test.util.isMatched

object MockHttpClientEngine {
    val format = ResourcesFormat()

    class Builder {
        val valid: MutableList<Pair<String, String>> = mutableListOf()
        val invalid: MutableList<Pair<String, HttpStatusCode>> = mutableListOf()
        val error: MutableList<Pair<String, Exception>> = mutableListOf()

        inline fun <reified T> mock(resource: T, expected: String): Builder {
            valid.add(href(format, resource) to expected)
            return this
        }

        inline fun <reified T> mock(resource: T, expected: HttpStatusCode): Builder {
            invalid.add(href(format, resource) to expected)
            return this
        }

        inline fun <reified T> mock(resource: T, exception: Exception): Builder {
            error.add(href(format, resource) to exception)
            return this
        }

        fun build() = MockEngine { request ->
            valid.forEach { (url, expected) ->
                if (request.url.toString().contains(url)) {
                    return@MockEngine respond(
                        content = ByteReadChannel(expected),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
            }

            invalid.forEach { (url, expected) ->
                if (request.url.toString().contains(url)) {
                    return@MockEngine respond(
                        content = ByteReadChannel("\"error\":\"${expected.description}\""),
                        status = expected,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
            }

            error.forEach { (url, exception) ->
                if (request.url.toString().contains(url)) {
                    throw exception
                }
            }

            mockError()
        }
    }

    inline fun <reified T> mock(resource: T, expected: String) = Builder().mock(resource, expected)
    inline fun <reified T> mock(resource: T, expected: HttpStatusCode) = Builder().mock(resource, expected)
    inline fun <reified T> mock(resource: T, exception: Exception) = Builder().mock(resource, exception)

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
