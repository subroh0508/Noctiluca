package noctiluca.api.instancessocial

import noctiluca.api.instancessocial.params.GetInstancesSearch

interface InstancesSocialApi {
    suspend fun search(
        query: String,
        count: Int = 20,
    ): GetInstancesSearch.Response
}
