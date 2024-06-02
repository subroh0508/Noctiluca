package noctiluca.network.mastodon.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import noctiluca.model.FileNotFoundException
import noctiluca.model.media.LocalMediaFile
import noctiluca.network.mastodon.Api
import noctiluca.network.mastodon.AuthorizationTokenProvider
import noctiluca.network.mastodon.MastodonApiV2
import noctiluca.network.mastodon.data.instance.NetworkV2Instance
import noctiluca.network.mastodon.data.mediaattachment.NetworkMediaAttachment
import noctiluca.network.mastodon.extensions.toByteArray

internal class MastodonApiV2Client(
    token: AuthorizationTokenProvider,
    private val client: HttpClient,
) : MastodonApiV2, AbstractMastodonApiClient(token) {
    override suspend fun getInstance(
        domain: String,
    ): NetworkV2Instance = client.get(
        Api.V2.Instance(),
        domain = domain,
        skipAuthorization = true,
    ).body()

    override suspend fun postMedia(
        media: LocalMediaFile,
    ): NetworkMediaAttachment {
        val data = media.original.toByteArray()
        if (data.isEmpty()) {
            throw FileNotFoundException("File not found: ${media.original}")
        }

        return client.post(
            Api.V2.Media(),
            parts = formData {
                append(
                    "file",
                    data,
                    Headers.build {
                        append(HttpHeaders.ContentType, media.mimeType)
                    },
                )
            },
        ).body()
    }
}
