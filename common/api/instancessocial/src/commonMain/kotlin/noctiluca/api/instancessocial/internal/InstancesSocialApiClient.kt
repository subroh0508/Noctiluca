package noctiluca.api.instancessocial.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import noctiluca.api.instancessocial.Api
import noctiluca.api.instancessocial.InstancesSocialApi
import noctiluca.api.instancessocial.params.GetInstancesSearch

internal class InstancesSocialApiClient(
    private val token: String,
    private val client: HttpClient,
) : InstancesSocialApi {
    override suspend fun search(
        query: String,
        count: Int,
    ): GetInstancesSearch.Response = client.get(
        Api.Instances.Search(),
    ) {
        bearerAuth(token)
        parameter("q", query)
    }.body()
}
