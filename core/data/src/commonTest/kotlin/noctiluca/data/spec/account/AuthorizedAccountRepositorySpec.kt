package noctiluca.data.spec.account

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.containExactly
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import noctiluca.data.TestDataComponent
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.account.impl.AuthorizedAccountRepositoryImpl
import noctiluca.data.json.JSON_ACCOUNT_CREDENTIAL_2
import noctiluca.data.json.JSON_ACCOUNT_CREDENTIAL_3
import noctiluca.data.mock.MockAccountDataStore
import noctiluca.data.mock.buildEmptyMockAccountDataStore
import noctiluca.datastore.AccountDataStore
import noctiluca.model.*
import noctiluca.model.account.Account
import noctiluca.network.mastodon.Api
import noctiluca.test.shared.DOMAIN_SAMPLE_COM
import noctiluca.test.shared.DUMMY_ACCESS_TOKEN
import noctiluca.test.shared.JSON_ACCOUNT_CREDENTIAL
import noctiluca.test.shared.URL_SAMPLE_COM
import noctiluca.test.shared.extension.flowToList
import noctiluca.test.shared.me
import noctiluca.test.shared.mock.MockAuthorizationTokenDataStore
import noctiluca.test.shared.mock.MockHttpClientEngine
import noctiluca.test.shared.mock.MockHttpClientEngine.mockError
import noctiluca.test.shared.mock.buildEmptyMockAuthenticationTokenDataStore
import noctiluca.test.shared.model.MockAuthorizedUser
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

    describe("#all") {
        context("when the local cache does not exists") {
            val repository = buildRepository(
                Api.V1.Accounts.VerifyCredentials(),
                listOf(),
                buildEmptyMockAuthenticationTokenDataStore(),
                buildEmptyMockAccountDataStore(),
            )

            it("returns flow with empty list") {
                flowToList(
                    repository.all(),
                ).first() should beEmpty()
            }
        }

        context("when the local cache exists") {
            context("and the local cache has only one account") {
                val mockAuthenticationTokenDataStore = MockAuthorizationTokenDataStore(
                    init = listOf(me),
                    getCache = { DUMMY_ACCESS_TOKEN to Domain(DOMAIN_SAMPLE_COM) },
                )

                val repository = buildRepository(
                    Api.V1.Accounts.VerifyCredentials(),
                    listOf(
                        DUMMY_ACCESS_TOKEN to JSON_ACCOUNT_CREDENTIAL,
                    ),
                    mockAuthenticationTokenDataStore,
                    MockAccountDataStore(listOf(current)),
                )

                it("returns flow with empty list") {
                    flowToList(
                        repository.all(),
                    ).first() should containExactly(
                        current.copy(displayName = "サンプル太郎"),
                    )
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
                            DUMMY_ACCESS_TOKEN to JSON_ACCOUNT_CREDENTIAL,
                            ACCESS_TOKENS[0] to JSON_ACCOUNT_CREDENTIAL_2,
                            ACCESS_TOKENS[1] to JSON_ACCOUNT_CREDENTIAL_3,
                        ),
                        mockAuthenticationTokenDataStore,
                        mockAccountDataStore,
                    )

                    it("returns flow with instances from server") {
                        flowToList(
                            repository.all(),
                        ).first() should containExactly(
                            accounts[0].copy(displayName = "サンプル太郎"),
                            accounts[1].copy(displayName = "サンプル次郎"),
                            accounts[2].copy(displayName = "サンプル三郎"),
                        )
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
                        flowToList(
                            repository.all(),
                        ).first() should containExactly(
                            accounts[0],
                            accounts[1],
                            accounts[2],
                        )
                    }
                }
            }
        }
    }
})

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
