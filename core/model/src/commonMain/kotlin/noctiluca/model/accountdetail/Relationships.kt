package noctiluca.model.accountdetail

@JvmInline
value class Relationships(val value: Set<Relationship>?) {
    companion object {
        operator fun invoke(
            vararg value: Relationship,
        ) = Relationships(value.toSet())

        operator fun invoke(
            value: Set<Relationship>?,
        ) = value?.let { Relationships(it) } ?: ME

        val ME = Relationships(null)
        val NONE = Relationships(setOf())
    }

    val me get() = this == ME
    val following get() = value?.contains(Relationship.FOLLOWING) ?: false
    val followedBy get() = value?.contains(Relationship.FOLLOWED_BY) ?: false
    val requested get() = value?.contains(Relationship.REQUESTED) ?: false
    val blocking get() = value?.contains(Relationship.BLOCKING) ?: false
    val blockedBy get() = value?.contains(Relationship.BLOCKED_BY) ?: false
    val muting get() = value?.contains(Relationship.MUTING) ?: false
}
