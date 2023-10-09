package noctiluca.timeline.domain.model

import noctiluca.model.StatusId
import noctiluca.status.model.Status
import noctiluca.status.model.StatusList
import noctiluca.timeline.model.HashTag as HashTagValue
import noctiluca.timeline.model.List as AccountList

sealed class Timeline {
    abstract val statuses: StatusList

    operator fun plus(list: StatusList): Timeline = when (this) {
        is Global -> copy(statuses = statuses + list)
        is Local -> copy(statuses = statuses + list)
        is Home -> copy(statuses = statuses + list)
        is HashTag -> copy(statuses = statuses + list)
        is List -> copy(statuses = statuses + list)
    }

    operator fun minus(id: StatusId): Timeline = when (this) {
        is Global -> copy(statuses = statuses.filterNot { it.id == id })
        is Local -> copy(statuses = statuses.filterNot { it.id == id })
        is Home -> copy(statuses = statuses.filterNot { it.id == id })
        is HashTag -> copy(statuses = statuses.filterNot { it.id == id })
        is List -> copy(statuses = statuses.filterNot { it.id == id })
    }

    fun insert(status: Status): Timeline {
        val index = statuses.indexOfFirst { it.id.value < status.id.value }.takeIf { it != -1 } ?: 0
        val next = statuses.take(index) + listOf(status) + statuses.drop(index)

        return when (this) {
            is Global -> copy(statuses = next)
            is Local -> copy(statuses = next)
            is Home -> copy(statuses = next)
            is HashTag -> copy(statuses = next)
            is List -> copy(statuses = next)
        }
    }

    fun replace(status: Status): Timeline {
        val index = statuses.indexOfFirst { it.id == status.id }
        val next = statuses.toMutableList().apply {
            set(index, status)
        }.toList()

        return when (this) {
            is Global -> copy(statuses = next)
            is Local -> copy(statuses = next)
            is Home -> copy(statuses = next)
            is HashTag -> copy(statuses = next)
            is List -> copy(statuses = next)
        }
    }

    data class Global(
        override val statuses: StatusList,
        val onlyRemote: Boolean,
        val onlyMedia: Boolean,
    ) : Timeline()

    data class Local(
        override val statuses: StatusList,
        val onlyMedia: Boolean,
    ) : Timeline()

    data class Home(
        override val statuses: StatusList,
    ) : Timeline()

    data class HashTag(
        override val statuses: StatusList,
        val hashtag: HashTagValue,
        val onlyMedia: Boolean,
    ) : Timeline()

    data class List(
        override val statuses: StatusList,
        val list: AccountList,
    ) : Timeline()
}
