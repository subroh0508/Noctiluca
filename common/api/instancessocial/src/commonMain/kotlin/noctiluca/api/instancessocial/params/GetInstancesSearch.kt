package noctiluca.api.instancessocial.params

import kotlinx.serialization.Serializable
import noctiluca.api.instancessocial.json.InstanceJson
import noctiluca.api.instancessocial.json.PaginationJson

abstract class GetInstancesSearch {
    companion object {
        internal const val ENDPOINT = "/instances/search"
    }

    @Serializable
    data class Response(
        val instances: List<InstanceJson>,
        val pagination: PaginationJson,
    )
}
