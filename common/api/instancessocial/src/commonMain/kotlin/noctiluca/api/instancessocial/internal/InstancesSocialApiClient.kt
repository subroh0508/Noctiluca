package noctiluca.api.instancessocial.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import noctiluca.api.instancessocial.InstancesSocialApi
import noctiluca.api.instancessocial.params.GetInstancesSearch

internal class InstancesSocialApiClient(
    private val token: String,
    private val client: HttpClient,
) : InstancesSocialApi {
    companion object {
        private const val API_VERSION = "/api/1.0"
    }

    override suspend fun search(
        query: String,
        count: Int,
    ) = client.get(API_VERSION + GetInstancesSearch.ENDPOINT) {
        bearerAuth(token)
        url { parameters["q"] = query }
    }.body<GetInstancesSearch.Response>()
}