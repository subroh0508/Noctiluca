package noctiluca.accountdetail.domain.model

import noctiluca.model.StatusId

sealed class StatusesQuery {
    abstract val maxId: StatusId?

    data class Default(
        override val maxId: StatusId? = null,
    ) : StatusesQuery()

    data class WithReplies(
        override val maxId: StatusId? = null,
    ) : StatusesQuery()

    data class OnlyMedia(
        override val maxId: StatusId? = null,
    ) : StatusesQuery()
}
