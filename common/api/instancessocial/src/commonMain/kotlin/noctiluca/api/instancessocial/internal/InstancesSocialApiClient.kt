package noctiluca.api.instancessocial.internal

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import noctiluca.api.instancessocial.InstancesSocialApi

internal class InstancesSocialApiClient(
    private val token: String,
    private val client: HttpClient,
) : InstancesSocialApi {
    companion object {
        private const val ENDPOINT_SEARCH = "/api/1.0/instances/search"
    }

    override suspend fun search(
        query: String,
        count: Int,
    ) = client.get(ENDPOINT_SEARCH) {
        bearerAuth(token)
        url { parameters["q"] = query }
    }.bodyAsText()
}