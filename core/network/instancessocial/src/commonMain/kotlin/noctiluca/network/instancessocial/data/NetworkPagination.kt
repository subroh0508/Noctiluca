package noctiluca.network.instancessocial.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPagination(
    val total: Int,
    @SerialName("next_id")
    val nextId: String?,
)
