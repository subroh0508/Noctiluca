package noctiluca.timeline.domain.model

import noctiluca.status.model.StatusList
import noctiluca.timeline.model.HashTag as HashTagValue

sealed class Timeline {
    abstract val statuses: StatusList

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
    ) : Timeline()
}
