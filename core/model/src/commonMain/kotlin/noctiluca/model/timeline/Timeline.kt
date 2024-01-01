package noctiluca.model.timeline

import noctiluca.model.StatusId
import noctiluca.model.status.Status
import noctiluca.model.status.StatusList
import noctiluca.model.timeline.HashTag as HashTagValue

sealed class Timeline {
    abstract val statuses: StatusList

    val maxId get() = statuses.lastOrNull()?.id

    operator fun plus(list: StatusList): Timeline {
        val next = (statuses + list).distinctBy { it.id }

        return when (this) {
            is Global -> copy(statuses = next)
            is Local -> copy(statuses = next)
            is Home -> copy(statuses = next)
            is HashTag -> copy(statuses = next)
            is List -> copy(statuses = next)
        }
    }

    operator fun minus(id: StatusId): Timeline = when (this) {
        is Global -> copy(statuses = statuses.filterNot { it.id == id })
        is Local -> copy(statuses = statuses.filterNot { it.id == id })
        is Home -> copy(statuses = statuses.filterNot { it.id == id })
        is HashTag -> copy(statuses = statuses.filterNot { it.id == id })
        is List -> copy(statuses = statuses.filterNot { it.id == id })
    }

    fun insert(status: Status): Timeline {
        if (statuses.find { it.id == status.id } != null) {
            return replace(status)
        }

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