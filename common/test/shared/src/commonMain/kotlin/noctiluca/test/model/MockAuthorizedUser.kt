package noctiluca.test.model

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.test.DOMAIN_MASTODON_JP

data class MockAuthorizedUser(
    override val id: AccountId = AccountId("1"),
    override val domain: Domain = Domain(DOMAIN_MASTODON_JP),
) : AuthorizedUser
