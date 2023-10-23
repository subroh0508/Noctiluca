package noctiluca.network.instancessocial

import noctiluca.network.instancessocial.params.GetInstancesSearch

interface InstancesSocialApi {
    suspend fun search(
        query: String,
        count: Int = 20,
    ): GetInstancesSearch.Response
}
