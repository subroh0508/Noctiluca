package noctiluca.timeline.domain.model

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
