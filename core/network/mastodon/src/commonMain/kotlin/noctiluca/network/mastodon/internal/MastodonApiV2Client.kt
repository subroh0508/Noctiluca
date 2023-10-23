package noctiluca.network.mastodon.internal

import io.ktor.client.*
import io.ktor.client.call.*
import noctiluca.network.mastodon.Api
import noctiluca.network.mastodon.MastodonApiV2
import noctiluca.network.mastodon.TokenProvider
import noctiluca.network.mastodon.json.instance.V2InstanceJson

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
