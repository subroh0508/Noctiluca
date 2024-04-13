package noctiluca.features.toot.model

import noctiluca.model.AuthorizeEventState
import noctiluca.model.account.Account

data class TootModel(
    val current: Account? = null,
) {
    constructor(
        state: AuthorizeEventState?,
        accounts: List<Account>,
    ) : this(
        current = accounts.find { it.id == state?.user?.id },
    )
}
