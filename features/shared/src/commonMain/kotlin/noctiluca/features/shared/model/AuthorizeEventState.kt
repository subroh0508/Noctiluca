package noctiluca.features.shared.model

import noctiluca.model.AuthorizedUser

data class AuthorizeEventState(
    val user: AuthorizedUser? = null,
    val event: Event = Event.OK,
) {
    enum class Event { OK, REOPEN, SIGN_IN }
}
