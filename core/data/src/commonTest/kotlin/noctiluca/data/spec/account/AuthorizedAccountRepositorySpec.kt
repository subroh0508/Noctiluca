package noctiluca.data.spec.account

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import noctiluca.data.TestDataComponent
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.account.impl.AuthorizedAccountRepositoryImpl
import noctiluca.data.json.JSON_ACCOUNT_CREDENTIAL_1
import noctiluca.data.mock.MockAccountDataStore
import noctiluca.datastore.AccountDataStore
import noctiluca.model.*
import noctiluca.model.account.Account
import noctiluca.network.mastodon.Api
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.URL_SAMPLE_COM
import noctiluca.test.me
import noctiluca.test.mock.MockAuthenticationTokenDataStore
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.test.model.MockAuthorizedUser
import org.koin.core.component.get

class AuthorizedAccountRepositorySpec : DescribeSpec({
    coroutineTestScope = true

    val current = Account(
        me.id,
        "test1",
        "テスト太郎",
        Uri("${me.baseUrl}/@test1"),
        Uri("${me.baseUrl}/accounts/avatars/avater.png"),
        "@test1@${me.domain.value}",
    )

    val accounts = listOf(
        current,
        Account(
            AccountId("10"),
            "test2",
            "テスト次郎",
            Uri("$URL_SAMPLE_COM/@test2"),
            Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
            "@test2@$DOMAIN_SAMPLE_COM",
        ),
        Account(
            AccountId("100"),
            "test3",
            "テスト三郎",
            Uri("$URL_SAMPLE_COM/@test3"),
            Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
            "@test3@$DOMAIN_SAMPLE_COM",
        ),
    )

    describe("#current") {
        context("when the local cache does not exist") {
            context("and the sever returns valid response") {
                val mockAccountDataStore = MockAccountDataStore()
                val repository = buildRepository(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                    MockAuthenticationTokenDataStore(me.id, me.domain),
                    mockAccountDataStore,
                )

                it("returns flow with one instance of account from server") {
                    runBlocking {
                        repository.current().first()
                    } should be(current.copy(displayName = "サンプル太郎") to me.domain)

                    runBlocking {
                        mockAccountDataStore.get(me.id)
                    } should be(current.copy(displayName = "サンプル太郎"))
                }
            }

            context("and current domain is not unknown") {
                val repository = buildRepository(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                )

                it("raises AuthorizedTokenNotFoundException") {
                    shouldThrowExactly<AuthorizedTokenNotFoundException> {
                        runBlocking { repository.current().first() }
                    }
                }
            }

            context("and the sever returns error response") {
                val repository = buildRepository(
                    Api.V1.Accounts.VerifyCredentials(),
                    HttpStatusCode.BadRequest,
                    MockAuthenticationTokenDataStore(me.id, me.domain),
                )

                it("raises HttpException") {
                    shouldThrowExactly<HttpException> {
                        runBlocking { repository.current().first() }
                    }
                }
            }
        }

        context("when the local cache exists") {
            val mockAuthenticationTokenDataStore = MockAuthenticationTokenDataStore(
                init = listOf(MockAuthorizedUser(me.id, me.domain)),
                getCache = { DUMMY_ACCESS_TOKEN to me.domain },
            )

            context("and the sever returns valid response") {
                val mockAccountDataStore = MockAccountDataStore(current)
                val repository = buildRepository(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                    mockAuthenticationTokenDataStore,
                    mockAccountDataStore,
                )

                it("returns flow with one instances of account from server") {
                    runBlocking {
                        repository.current().first()
                    } should be(current.copy(displayName = "サンプル太郎") to me.domain)

                    runBlocking {
                        mockAccountDataStore.get(me.id)
                    } should be(current.copy(displayName = "サンプル太郎"))
                }
            }

            context("and the sever returns error response") {
                val mockAccountDataStore = MockAccountDataStore(current)
                val repository = buildRepository(
                    Api.V1.Accounts.VerifyCredentials(),
                    HttpStatusCode.BadRequest,
                    mockAuthenticationTokenDataStore,
                    mockAccountDataStore,
                )

                it("returns flow with one instance from cache") {
                    runBlocking {
                        repository.current().first()
                    } should be(current to me.domain)
                }
            }
        }
    }
})

private inline fun <reified T> buildRepository(
    resource: T,
    expected: String,
    mockAuthenticationTokenDataStore: MockAuthenticationTokenDataStore = MockAuthenticationTokenDataStore(),
    mockAccountDataStore: AccountDataStore = MockAccountDataStore(),
) = buildRepository(
    MockHttpClientEngine(resource, expected),
    mockAuthenticationTokenDataStore,
    mockAccountDataStore,
)

private inline fun <reified T> buildRepository(
    resource: T,
    errorStatusCode: HttpStatusCode,
    mockAuthenticationTokenDataStore: MockAuthenticationTokenDataStore = MockAuthenticationTokenDataStore(),
    mockAccountDataStore: AccountDataStore = MockAccountDataStore(),
) = buildRepository(
    MockHttpClientEngine(resource, errorStatusCode),
    mockAuthenticationTokenDataStore,
    mockAccountDataStore,
)

private fun buildRepository(
    mockEngine: MockEngine,
    mockAuthenticationTokenDataStore: MockAuthenticationTokenDataStore = MockAuthenticationTokenDataStore(),
    mockAccountDataStore: AccountDataStore = MockAccountDataStore(),
): AuthorizedAccountRepository {
    val component = TestDataComponent(
        mockEngine,
        mockAuthenticationTokenDataStore,
        mockAccountDataStore,
    )

    return AuthorizedAccountRepositoryImpl(
        component.get(),
        component.get(),
        component.get(),
    )
}
