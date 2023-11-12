package noctiluca.network.instancessocial.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import noctiluca.model.HttpException
import noctiluca.model.HttpUnauthorizedException
import noctiluca.network.instancessocial.Api
import noctiluca.network.instancessocial.InstancesSocialApi
import noctiluca.network.instancessocial.params.GetInstancesSearch

internal class InstancesSocialApiClient(
    private val token: String,
    private val client: HttpClient,
) : InstancesSocialApi {
    override suspend fun search(
        query: String,
        count: Int,
    ): GetInstancesSearch.Response = handleResponseException {
        client.get(
            Api.Instances.Search(),
        ) {
            bearerAuth(token)
            parameter("q", query)
        }
    }.body()

    private inline fun handleResponseException(
        block: () -> HttpResponse,
    ) = runCatching { block() }.getOrElse { e ->
        if (e is ResponseException) {
            throw HttpException(e.response.status.value, e)
        }

        throw e
    }
}
