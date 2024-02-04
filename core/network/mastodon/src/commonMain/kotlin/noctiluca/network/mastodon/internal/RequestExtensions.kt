package noctiluca.network.mastodon.internal

import io.ktor.client.request.*

internal fun <T> HttpRequestBuilder.parameter(
    key: String,
    list: List<T>
) = list.forEach { parameter("$key[]", it) }
