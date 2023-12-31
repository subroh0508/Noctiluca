package noctiluca.model.timeline

data class AccountList(
    val id: ListId,
    val title: String,
    val policy: RepliesPolicy,
) {
    enum class RepliesPolicy { FOLLOWED, LIST, NONE }
}
