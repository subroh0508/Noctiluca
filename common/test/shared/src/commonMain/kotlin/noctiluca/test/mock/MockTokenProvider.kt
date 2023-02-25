package noctiluca.test.mock

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.repository.TokenProvider
import noctiluca.test.me

class MockTokenProvider(
    private val current: AuthorizedUser = me,
    private val authorizedUsers: List<AuthorizedUser> = listOf(me),
) : TokenProvider {
    override suspend fun getCurrent() = current
    override suspend fun getAuthorizedUsers() = authorizedUsers
    override suspend fun switch(id: AccountId) = me
    override suspend fun expireCurrent() = Unit
}
