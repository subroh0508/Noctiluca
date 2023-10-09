package noctiluca.api.mastodon.internal

import io.ktor.client.*
import io.ktor.client.call.*
import noctiluca.api.mastodon.Api
import noctiluca.api.mastodon.MastodonApiV2
import noctiluca.api.mastodon.TokenProvider
import noctiluca.api.mastodon.json.instance.V2InstanceJson

internal class MastodonApiV2Client(
    override val token: TokenProvider,
    override val client: HttpClient,
) : MastodonApiV2, AbstractMastodonApiClient() {
    override suspend fun getInstance(
        domain: String,
    ): V2InstanceJson = client.get(
        Api.V2.Instance(),
        domain = domain,
        skipAuthorization = true,
    ).body()
}
