package noctiluca.timeline.domain.usecase.spec

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.containExactly
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.account.Account
import noctiluca.network.mastodon.Api
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.URL_SAMPLE_COM
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.test.mock.MockHttpClientEngine.mockError
import noctiluca.timeline.domain.TestTimelineUseCaseComponent
import noctiluca.timeline.domain.mock.MockLocalAuthorizedAccountRepository
import noctiluca.timeline.domain.usecase.FetchAllAuthorizedAccountsUseCase
import noctiluca.timeline.domain.usecase.json.JSON_ACCOUNT_CREDENTIAL_2
import noctiluca.timeline.domain.usecase.json.JSON_ACCOUNT_CREDENTIAL_3

private val ACCESS_TOKENS = listOf("yyy", "zzz")

class FetchAllAuthorizedAccountsUseCaseSpec : DescribeSpec({
    val accounts = listOf(
        Account(
            AccountId("1"),
            "test1",
            "テスト太郎",
            Uri("$URL_SAMPLE_COM/@test1"),
            Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
            "@test1@$DOMAIN_SAMPLE_COM",
        ),
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

    describe("#execute") {
        context("when the local cache does not exists") {
            val localRepository = MockLocalAuthorizedAccountRepository()
            val useCase = buildUseCase(
                noctiluca.network.mastodon.Api.V1.Accounts.VerifyCredentials(),
                listOf(),
                localRepository,
            )

            it("returns empty flow") {
                runBlocking { useCase.execute().toList() } should beEmpty()
            }
        }

        context("when the local cache exists") {
            val current = Account(
                AccountId("1"),
                "test1",
                "テスト太郎",
                Uri("$URL_SAMPLE_COM/@test1"),
                Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
                "@test1@$DOMAIN_SAMPLE_COM",
            ) to Domain(DOMAIN_SAMPLE_COM)

            context("and the local cache has only one account") {
                val localRepository = MockLocalAuthorizedAccountRepository(
                    current = current,
                    accessToken = { DUMMY_ACCESS_TOKEN to Domain(DOMAIN_SAMPLE_COM) },
                )

                val useCase = buildUseCase(
                    noctiluca.network.mastodon.Api.V1.Accounts.VerifyCredentials(),
                    listOf(),
                    localRepository,
                )

                it("returns empty flow") {
                    runBlocking { useCase.execute().toList() } should beEmpty()
                }
            }

            context("and the local cache has multiple accounts") {
                val localRepository = MockLocalAuthorizedAccountRepository(
                    current = current,
                    accessToken = {
                        when (it) {
                            accounts[0].id -> DUMMY_ACCESS_TOKEN to Domain(DOMAIN_SAMPLE_COM)
                            accounts[1].id -> ACCESS_TOKENS[0] to Domain(DOMAIN_SAMPLE_COM)
                            accounts[2].id -> ACCESS_TOKENS[1] to Domain(DOMAIN_SAMPLE_COM)
                            else -> null
                        }
                    },
                    all = accounts,
                )

                context("and the sever returns valid response") {
                    val useCase = buildUseCase(
                        noctiluca.network.mastodon.Api.V1.Accounts.VerifyCredentials(),
                        listOf(
                            ACCESS_TOKENS[0] to JSON_ACCOUNT_CREDENTIAL_2,
                            ACCESS_TOKENS[1] to JSON_ACCOUNT_CREDENTIAL_3,
                        ),
                        localRepository,
                    )

                    it("returns flow with instances from cache and server") {
                        runBlocking { useCase.execute().toList() }.let {
                            it should haveSize(4)
                            it should containExactly(
                                accounts[1],
                                accounts[1].copy(displayName = "サンプル次郎"),
                                accounts[2],
                                accounts[2].copy(displayName = "サンプル三郎"),
                            )
                        }
                    }
                }
                context("and the sever returns error response") {
                    val useCase = buildUseCase(
                        noctiluca.network.mastodon.Api.V1.Accounts.VerifyCredentials(),
                        HttpStatusCode.BadRequest,
                        localRepository,
                    )

                    it("returns flow with instances from cache and server") {
                        runBlocking { useCase.execute().toList() }.let {
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

private inline fun <reified T> buildUseCase(
    resource: T,
    expected: List<Pair<String, String>>,
    localRepository: MockLocalAuthorizedAccountRepository,
): FetchAllAuthorizedAccountsUseCase = TestTimelineUseCaseComponent(
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
    localRepository,
).scope.get()

private inline fun <reified T> buildUseCase(
    resource: T,
    errorStatusCode: HttpStatusCode,
    localRepository: MockLocalAuthorizedAccountRepository,
): FetchAllAuthorizedAccountsUseCase = TestTimelineUseCaseComponent(
    MockHttpClientEngine(resource, errorStatusCode),
    localRepository,
).scope.get()
