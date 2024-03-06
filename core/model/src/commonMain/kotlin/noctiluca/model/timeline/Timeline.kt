package noctiluca.model.timeline

import noctiluca.model.StatusId
import noctiluca.model.status.Status
import noctiluca.model.status.StatusList
import noctiluca.model.timeline.HashTag as HashTagValue

sealed class Timeline {
    abstract val statuses: StatusList
    abstract val isActive: Boolean

    val maxId get() = statuses.lastOrNull()?.id

    operator fun plus(
        list: StatusList,
    ) = replace((statuses + list).distinctBy { it.id })

    operator fun minus(
        id: StatusId,
    ) = replace(statuses.filterNot { it.id == id })

    fun insert(status: Status): Timeline {
        if (statuses.find { it.id == status.id } != null) {
            return replace(status)
        }

        val index = statuses.indexOfFirst { it.id.value < status.id.value }
            .takeIf { it != -1 } ?: 0
        val next = statuses.take(index) + listOf(status) + statuses.drop(index)

        return replace(next)
    }

    fun replace(status: Status): Timeline {
        val index = statuses.indexOfFirst { it.id == status.id }
            .takeIf { it != -1 } ?: return this
        val next = statuses.toMutableList().apply {
            set(index, status)
        }.toList()

        return replace(next)
    }

    fun favourite(
        status: Status,
    ) = replace(
        statuses.map {
            if (it.id != status.id) {
                return@map it
            }

            it.copy(
                favourited = !status.favourited,
                favouriteCount = status.favouriteCount + if (status.favourited) -1 else 1,
            )
        },
    )

    fun boost(
        status: Status,
    ) = replace(
        statuses.map {
            if (it.id != status.id) {
                return@map it
            }

            it.copy(
                reblogged = !status.reblogged,
                reblogCount = status.reblogCount + if (status.reblogged) -1 else 1,
            )
        },
    )

    fun bookmark(
        status: Status,
    ) = replace(
        statuses.map {
            if (it.id != status.id) {
                return@map it
            }

            it.copy(bookmarked = !status.bookmarked)
        },
    )

    fun activate(isActive: Boolean) = when (this) {
        is Global -> copy(isActive = isActive)
        is Local -> copy(isActive = isActive)
        is Home -> copy(isActive = isActive)
        is HashTag -> copy(isActive = isActive)
        is List -> copy(isActive = isActive)
    }

    private fun replace(statuses: StatusList) = when (this) {
        is Global -> copy(statuses = statuses)
        is Local -> copy(statuses = statuses)
        is Home -> copy(statuses = statuses)
        is HashTag -> copy(statuses = statuses)
        is List -> copy(statuses = statuses)
    }

    data class Global(
        override val statuses: StatusList,
        val onlyRemote: Boolean,
        val onlyMedia: Boolean,
        override val isActive: Boolean,
    ) : Timeline()

    data class Local(
        override val statuses: StatusList,
        val onlyMedia: Boolean,
        override val isActive: Boolean,
    ) : Timeline()

    data class Home(
        override val statuses: StatusList,
        override val isActive: Boolean,
    ) : Timeline()

    data class HashTag(
        override val statuses: StatusList,
        val hashtag: HashTagValue,
        val onlyMedia: Boolean,
        override val isActive: Boolean,
    ) : Timeline()

    data class List(
        override val statuses: StatusList,
        val list: AccountList,
        override val isActive: Boolean,
    ) : Timeline()
}
