package noctiluca.data.spec.account

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.containExactly
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import noctiluca.data.TestDataComponent
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.account.impl.AuthorizedAccountRepositoryImpl
import noctiluca.data.json.JSON_ACCOUNT_CREDENTIAL_1
import noctiluca.data.json.JSON_ACCOUNT_CREDENTIAL_2
import noctiluca.data.json.JSON_ACCOUNT_CREDENTIAL_3
import noctiluca.data.mock.MockAccountDataStore
import noctiluca.data.mock.buildEmptyMockAccountDataStore
import noctiluca.datastore.AccountDataStore
import noctiluca.model.*
import noctiluca.model.account.Account
import noctiluca.network.mastodon.Api
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.URL_SAMPLE_COM
import noctiluca.test.me
import noctiluca.test.mock.*
import noctiluca.test.mock.MockHttpClientEngine.mockError
import noctiluca.test.model.MockAuthorizedUser
import org.koin.core.component.get

private val ACCESS_TOKENS = listOf("yyy", "zzz")

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
                val mockAccountDataStore = buildEmptyMockAccountDataStore()
                val repository = buildRepository(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                    buildFilledMockAuthenticationTokenDataStore(),
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
                    buildEmptyMockAuthenticationTokenDataStore(),
                    buildEmptyMockAccountDataStore(),
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
                    buildFilledMockAuthenticationTokenDataStore(),
                    buildEmptyMockAccountDataStore(),
                )

                it("raises HttpException") {
                    shouldThrowExactly<HttpException> {
                        runBlocking { repository.current().first() }
                    }
                }
            }
        }

        context("when the local cache exists") {
            val mockAuthenticationTokenDataStore = buildFilledMockAuthenticationTokenDataStore()

            context("and the sever returns valid response") {
                val mockAccountDataStore = MockAccountDataStore(listOf(current))
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
                val mockAccountDataStore = MockAccountDataStore(listOf(current))
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

    describe("#othres") {
        context("when the local cache does not exists") {
            val repository = buildRepository(
                Api.V1.Accounts.VerifyCredentials(),
                listOf(),
                buildEmptyMockAuthenticationTokenDataStore(),
                buildEmptyMockAccountDataStore(),
            )

            it("returns flow with empty list") {
                runBlocking {
                    repository.others().first()
                } should beEmpty()
            }
        }

        context("when the local cache exists") {
            context("and the local cache has only one account") {
                val repository = buildRepository(
                    Api.V1.Accounts.VerifyCredentials(),
                    listOf(),
                    buildFilledMockAuthenticationTokenDataStore(),
                    MockAccountDataStore(listOf(current)),
                )

                it("returns flow with empty list") {
                    runBlocking {
                        repository.others().first()
                    } should beEmpty()
                }
            }

            context("and the local cache has multiple accounts") {
                context("and the sever returns valid response") {
                    val mockAuthenticationTokenDataStore = MockAuthorizationTokenDataStore(
                        init = accounts.map { MockAuthorizedUser(it.id, Domain(DOMAIN_SAMPLE_COM)) },
                        getCache = {
                            when (it) {
                                accounts[0].id -> DUMMY_ACCESS_TOKEN to Domain(DOMAIN_SAMPLE_COM)
                                accounts[1].id -> ACCESS_TOKENS[0] to Domain(DOMAIN_SAMPLE_COM)
                                accounts[2].id -> ACCESS_TOKENS[1] to Domain(DOMAIN_SAMPLE_COM)
                                else -> null
                            }
                        },
                    )
                    val mockAccountDataStore = MockAccountDataStore(accounts)

                    val repository = buildRepository(
                        Api.V1.Accounts.VerifyCredentials(),
                        listOf(
                            ACCESS_TOKENS[0] to JSON_ACCOUNT_CREDENTIAL_2,
                            ACCESS_TOKENS[1] to JSON_ACCOUNT_CREDENTIAL_3,
                        ),
                        mockAuthenticationTokenDataStore,
                        mockAccountDataStore,
                    )

                    it("returns flow with instances from server") {
                        runBlocking {
                            repository.others().first()
                        }.let {
                            it should haveSize(2)
                            it should containExactly(
                                accounts[1].copy(displayName = "サンプル次郎"),
                                accounts[2].copy(displayName = "サンプル三郎"),
                            )
                        }
                    }
                }
                context("and the sever returns error response") {
                    val mockAuthenticationTokenDataStore = MockAuthorizationTokenDataStore(
                        init = accounts.map { MockAuthorizedUser(it.id, Domain(DOMAIN_SAMPLE_COM)) },
                        getCache = {
                            when (it) {
                                accounts[0].id -> DUMMY_ACCESS_TOKEN to Domain(DOMAIN_SAMPLE_COM)
                                accounts[1].id -> ACCESS_TOKENS[0] to Domain(DOMAIN_SAMPLE_COM)
                                accounts[2].id -> ACCESS_TOKENS[1] to Domain(DOMAIN_SAMPLE_COM)
                                else -> null
                            }
                        },
                    )
                    val mockAccountDataStore = MockAccountDataStore(init = accounts)

                    val repository = buildRepository(
                        Api.V1.Accounts.VerifyCredentials(),
                        errorStatusCode = HttpStatusCode.BadRequest,
                        mockAuthenticationTokenDataStore,
                        mockAccountDataStore,
                    )

                    it("returns flow with instances from cache") {
                        runBlocking {
                            repository.others().first()
                        }.let {
                            it should haveSize(2)
                            it should containExactly(
                                accounts[1],
                                accounts[2],
                            )
                        }
                    }
                }
            }
        }
    }
})

private inline fun <reified T> buildRepository(
    resource: T,
    expected: String,
    mockAuthenticationTokenDataStore: MockAuthorizationTokenDataStore,
    mockAccountDataStore: AccountDataStore,
) = buildRepository(
    MockHttpClientEngine(resource, expected),
    mockAuthenticationTokenDataStore,
    mockAccountDataStore,
)

private inline fun <reified T> buildRepository(
    resource: T,
    expected: List<Pair<String, String>>,
    mockAuthenticationTokenDataStore: MockAuthorizationTokenDataStore,
    mockAccountDataStore: AccountDataStore,
) = buildRepository(
    MockHttpClientEngine(
        resource,
    ) { request ->
        expected.forEach { (token, json) ->
            val authorization = request.headers[HttpHeaders.Authorization]
            if (authorization == "Bearer $token") {
                return@MockHttpClientEngine respond(
                    content = ByteReadChannel(json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
        }

        mockError()
    },
    mockAuthenticationTokenDataStore,
    mockAccountDataStore,
)

private inline fun <reified T> buildRepository(
    resource: T,
    errorStatusCode: HttpStatusCode,
    mockAuthenticationTokenDataStore: MockAuthorizationTokenDataStore,
    mockAccountDataStore: AccountDataStore,
) = buildRepository(
    MockHttpClientEngine(resource, errorStatusCode),
    mockAuthenticationTokenDataStore,
    mockAccountDataStore,
)

private fun buildRepository(
    mockEngine: MockEngine,
    mockAuthenticationTokenDataStore: MockAuthorizationTokenDataStore,
    mockAccountDataStore: AccountDataStore,
): AuthorizedAccountRepository {
    val component = TestDataComponent(
        mockEngine,
        mockAuthenticationTokenDataStore,
    ) {
        single<AccountDataStore> { mockAccountDataStore }
    }

    return AuthorizedAccountRepositoryImpl(
        component.get(),
        component.get(),
        component.get(),
    )
}
