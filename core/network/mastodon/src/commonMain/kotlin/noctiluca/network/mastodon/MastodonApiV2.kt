package noctiluca.network.mastodon

import noctiluca.network.mastodon.json.instance.V2InstanceJson

interface MastodonApiV2 {
    suspend fun getInstance(
        domain: String,
    ): V2InstanceJson
}
