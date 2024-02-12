package noctiluca.model

data class AuthorizeEventState(
    val user: AuthorizedUser? = null,
    val event: Event = Event.OK,
) {
    enum class Event { OK, REOPEN, SIGN_IN }
}
