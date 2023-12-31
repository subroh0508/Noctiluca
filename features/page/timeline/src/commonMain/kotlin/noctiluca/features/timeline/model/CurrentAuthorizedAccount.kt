package noctiluca.features.timeline.model

import noctiluca.model.Domain
import noctiluca.model.account.Account

data class CurrentAuthorizedAccount(
    val current: Account? = null,
    val domain: Domain? = null,
    val others: List<Account> = emptyList(),
) {
    constructor(
        item: Pair<Account, Domain>?,
        others: List<Account>,
    ) : this(
        current = item?.first,
        domain = item?.second,
        others = others,
    )
}
