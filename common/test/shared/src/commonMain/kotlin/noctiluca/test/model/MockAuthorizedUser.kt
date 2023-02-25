package noctiluca.test.model

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

data class MockAuthorizedUser(
    override val id: AccountId,
    override val domain: Domain,
) : AuthorizedUser
