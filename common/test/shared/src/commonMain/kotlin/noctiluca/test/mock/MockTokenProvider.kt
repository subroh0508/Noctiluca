package noctiluca.test.mock

import noctiluca.model.AccountId
import noctiluca.repository.TokenProvider
import noctiluca.test.model.MockAuthorizedUser

class MockTokenProvider : TokenProvider {
    override suspend fun getCurrent() = MockAuthorizedUser()
    override suspend fun getAuthorizedUsers() = listOf(MockAuthorizedUser())
    override suspend fun switch(id: AccountId) = MockAuthorizedUser()
    override suspend fun expireCurrent() = Unit
}