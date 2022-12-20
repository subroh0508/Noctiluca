package noctiluca.api.mastodon

import noctiluca.api.mastodon.params.GetInstance

interface MastodonApi {
    suspend fun getInstance(
        hostname: String,
    ): GetInstance.Response
}