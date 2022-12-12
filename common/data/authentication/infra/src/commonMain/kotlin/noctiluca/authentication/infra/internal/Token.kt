package noctiluca.authentication.infra.internal

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Hostname

internal data class Token(
    override val id: AccountId,
    override val hostname: Hostname,
    val accessToken: String,
) : AuthorizedUser {
    constructor(cached: CachedToken) : this(
        AccountId(cached.accountId),
        Hostname(cached.hostname),
        cached.accessToken,
    )
}
