package noctiluca.network.instancessocial.params

import kotlinx.serialization.Serializable
import noctiluca.network.instancessocial.data.NetworkInstance
import noctiluca.network.instancessocial.data.NetworkPagination

sealed interface GetInstancesSearch {
    @Serializable
    data class Response(
        val instances: List<NetworkInstance>,
        val pagination: NetworkPagination,
    )
}
