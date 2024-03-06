package noctiluca.features.timeline.model

import noctiluca.model.AuthorizeEventState
import noctiluca.model.Domain
import noctiluca.model.account.Account

data class AuthorizedAccountModel(
    val current: Account? = null,
    val domain: Domain? = null,
    val others: List<Account> = emptyList(),
) {
    constructor(
        state: AuthorizeEventState?,
        accounts: List<Account>,
    ) : this(
        current = accounts.find { it.id == state?.user?.id },
        domain = state?.user?.domain,
        others = accounts.filterNot { it.id == state?.user?.id },
    )
}
