package noctiluca.api.mastodon

import noctiluca.api.mastodon.json.instance.V2InstanceJson

interface MastodonApiV2 {
    suspend fun getInstance(
        domain: String,
    ): V2InstanceJson
}
