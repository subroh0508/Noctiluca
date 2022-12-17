package noctiluca.api.authentication.internal

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import noctiluca.api.authentication.InstancesSocialApi

internal class InstancesSocialApiClient(
    private val client: HttpClient,
) : InstancesSocialApi {
    companion object {
        private const val ENDPOINT_SEARCH = "/search"
    }

    override suspend fun search(
        query: String,
        count: Int,
    ) = client.get(ENDPOINT_SEARCH).bodyAsText()
}