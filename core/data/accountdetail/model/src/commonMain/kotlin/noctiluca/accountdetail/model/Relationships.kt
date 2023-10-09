package noctiluca.accountdetail.model

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
}
