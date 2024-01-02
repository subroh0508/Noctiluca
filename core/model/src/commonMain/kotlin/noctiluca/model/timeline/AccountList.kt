package noctiluca.model.timeline

data class AccountList(
    val title: String,
    val policy: RepliesPolicy,
) {
    enum class RepliesPolicy { FOLLOWED, LIST, NONE }
}
