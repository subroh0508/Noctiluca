package noctiluca.api.instancessocial.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationJson(
    val total: Int,
    @SerialName("next_id")
    val nextId: String?,
)
