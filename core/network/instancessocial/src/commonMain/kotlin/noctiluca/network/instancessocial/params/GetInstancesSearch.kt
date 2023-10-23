package noctiluca.network.instancessocial.params

import kotlinx.serialization.Serializable
import noctiluca.network.instancessocial.json.InstanceJson
import noctiluca.network.instancessocial.json.PaginationJson

sealed interface GetInstancesSearch {
    @Serializable
    data class Response(
        val instances: List<InstanceJson>,
        val pagination: PaginationJson,
    )
}
