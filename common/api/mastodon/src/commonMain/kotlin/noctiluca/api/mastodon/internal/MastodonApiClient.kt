package noctiluca.api.mastodon.internal

import io.ktor.client.*

internal class MastodonApiClient(
    private val token: String,
    private val client: HttpClient,
) {
}