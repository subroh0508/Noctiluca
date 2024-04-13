package noctiluca.features.toot.model

import noctiluca.model.AuthorizeEventState
import noctiluca.model.account.Account
import noctiluca.model.status.Status

data class TootModel(
    val current: Account? = null,
    val statusText: StatusText = StatusText(),
) {
    constructor(
        state: AuthorizeEventState?,
        accounts: List<Account>,
        statusText: StatusText,
    ) : this(
        current = accounts.find { it.id == state?.user?.id },
        statusText = statusText,
    )

    data class StatusText(
        val content: String? = null,
        val warning: String? = null,
        val visibility: Status.Visibility = Status.Visibility.PUBLIC,
        val sensitive: Boolean = false,
    )
}
