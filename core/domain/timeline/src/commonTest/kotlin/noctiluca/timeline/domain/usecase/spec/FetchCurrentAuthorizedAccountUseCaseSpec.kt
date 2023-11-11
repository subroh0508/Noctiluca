package noctiluca.timeline.domain.usecase.spec

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.containExactly
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import io.ktor.http.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import noctiluca.datastore.AccountDataStore
import noctiluca.model.*
import noctiluca.model.account.Account
import noctiluca.network.mastodon.Api
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.URL_SAMPLE_COM
import noctiluca.test.mock.MockAuthenticationTokenDataStore
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.test.model.MockAuthorizedUser
import noctiluca.timeline.domain.TestTimelineUseCaseComponent
import noctiluca.timeline.domain.mock.MockAccountDataStore
import noctiluca.timeline.domain.usecase.FetchCurrentAuthorizedAccountUseCase
import noctiluca.timeline.domain.usecase.json.JSON_ACCOUNT_CREDENTIAL_1

class FetchCurrentAuthorizedAccountUseCaseSpec : DescribeSpec({
    val account = Account(
        AccountId("1"),
        "test1",
        "テスト太郎",
        Uri("$URL_SAMPLE_COM/@test1"),
        Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
        "@test1@$DOMAIN_SAMPLE_COM",
    )

    describe("#execute") {
        context("when the local cache does not exist") {
            context("and the sever returns valid response") {
                val mockAccountDataStore = MockAccountDataStore()
                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                    MockAuthenticationTokenDataStore(account.id, Domain(DOMAIN_SAMPLE_COM)),
                    mockAccountDataStore,
                )

                it("returns flow with one instance of account") {
                    runBlocking { useCase.execute().toList() }.let {
                        it should haveSize(1)
                        it.first().let { (account, domain) ->
                            account should be(account.copy(displayName = "サンプル太郎"))
                            domain should be(Domain(DOMAIN_SAMPLE_COM))
                        }
                    }

                    runBlocking {
                        mockAccountDataStore.get(account.id)
                    } should be(account.copy(displayName = "サンプル太郎"))
                }
            }

            context("and current domain is not unknown") {
                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                )

                it("raises AuthorizedAccountNotFoundException") {
                    val instances: MutableList<Pair<Account, Domain>> = mutableListOf()

                    shouldThrowExactly<AuthorizedTokenNotFoundException> {
                        runBlocking { useCase.execute().collect { instances.add(it) } }
                    }
                    instances should haveSize(0)
                }
            }

            context("and the sever returns error response") {
                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    HttpStatusCode.BadRequest,
                    MockAuthenticationTokenDataStore(account.id, Domain(DOMAIN_SAMPLE_COM)),
                    MockAccountDataStore(),
                )

                it("raises AuthorizedAccountNotFoundException") {
                    val instances: MutableList<Pair<Account, Domain>> = mutableListOf()

                    shouldThrowExactly<AuthorizedTokenNotFoundException> {
                        runBlocking { useCase.execute().collect { instances.add(it) } }
                    }
                    instances should haveSize(0)
                }
            }
        }

        context("when the local cache exists") {
            val mockAuthenticationTokenDataStore = MockAuthenticationTokenDataStore(
                init = listOf(MockAuthorizedUser(account.id, Domain(DOMAIN_SAMPLE_COM))),
                getCache = { DUMMY_ACCESS_TOKEN to Domain(DOMAIN_SAMPLE_COM) },
            )
            val mockAccountDataStore = MockAccountDataStore(account)

            context("and the sever returns valid response") {
                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                    mockAuthenticationTokenDataStore,
                    mockAccountDataStore,
                )

                it("returns flow with two instances of account") {
                    runBlocking { useCase.execute().toList() }.let {
                        it should haveSize(2)
                        it should containExactly(
                            account to Domain(DOMAIN_SAMPLE_COM),
                            account.copy(displayName = "サンプル太郎") to Domain(DOMAIN_SAMPLE_COM),
                        )
                    }

                    runBlocking {
                        mockAccountDataStore.get(account.id)
                    } should be(account.copy(displayName = "サンプル太郎"))
                }
            }
            context("and the sever returns error response") {
                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    HttpStatusCode.BadRequest,
                    mockAuthenticationTokenDataStore,
                    mockAccountDataStore,
                )

                it("returns flow with one instances of account") {
                    val instances: MutableList<Pair<Account, Domain>> = mutableListOf()

                    shouldThrowAny {
                        useCase.execute().collect { instances.add(it) }
                    }

                    instances.let {
                        it should haveSize(1)
                        it.first().let { (account, domain) ->
                            account should be(account)
                            domain should be(Domain(DOMAIN_SAMPLE_COM))
                        }
                    }
                }
            }
        }
    }
})

private inline fun <reified T> buildUseCase(
    resource: T,
    expected: String,
    mockAuthenticationTokenDataStore: MockAuthenticationTokenDataStore = MockAuthenticationTokenDataStore(),
    mockAccountDataStore: AccountDataStore = MockAccountDataStore(),
): FetchCurrentAuthorizedAccountUseCase = TestTimelineUseCaseComponent(
    MockHttpClientEngine(resource, expected),
    mockAuthenticationTokenDataStore,
    mockAccountDataStore,
).scope.get()

private inline fun <reified T> buildUseCase(
    resource: T,
    errorStatusCode: HttpStatusCode,
    mockAuthenticationTokenDataStore: MockAuthenticationTokenDataStore = MockAuthenticationTokenDataStore(),
    mockAccountDataStore: AccountDataStore = MockAccountDataStore(),
): FetchCurrentAuthorizedAccountUseCase = TestTimelineUseCaseComponent(
    MockHttpClientEngine(resource, errorStatusCode),
    mockAuthenticationTokenDataStore,
    mockAccountDataStore,
).scope.get()
