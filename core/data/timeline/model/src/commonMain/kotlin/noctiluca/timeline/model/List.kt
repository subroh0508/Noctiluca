package noctiluca.timeline.model

data class List(
    val id: ListId,
    val title: String,
    val policy: RepliesPolicy,
) {
    enum class RepliesPolicy { FOLLOWED, LIST, NONE }
}
