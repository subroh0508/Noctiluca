package noctiluca.network.mastodon

import noctiluca.network.mastodon.data.instance.NetworkV2Instance

interface MastodonApiV2 {
    suspend fun getInstance(
        domain: String,
    ): NetworkV2Instance
}
