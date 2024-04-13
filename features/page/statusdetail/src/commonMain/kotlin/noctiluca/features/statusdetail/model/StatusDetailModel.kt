package noctiluca.features.statusdetail.model

import noctiluca.features.shared.model.MessageHolder
import noctiluca.model.status.StatusList

data class StatusDetailModel(
    val statuses: StatusList = listOf(),
    val loading: Boolean = false,
    val message: MessageHolder<Message> = MessageHolder(),
)
