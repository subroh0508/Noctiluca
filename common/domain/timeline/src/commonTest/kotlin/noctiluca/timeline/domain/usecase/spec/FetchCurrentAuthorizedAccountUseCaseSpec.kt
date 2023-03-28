package noctiluca.timeline.domain.usecase.spec

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import noctiluca.account.model.Account
import noctiluca.api.mastodon.Api
import noctiluca.model.*
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.URL_SAMPLE_COM
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.timeline.domain.TestTimelineUseCaseComponent
import noctiluca.timeline.domain.mock.MockLocalAuthorizedAccountRepository
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
                val localRepository = MockLocalAuthorizedAccountRepository(
                    account = null,
                    domain = Domain(DOMAIN_SAMPLE_COM),
                )
                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                    localRepository,
                )

                it("returns flow with one instance of account") {
                    runBlocking { useCase.execute().toList() }.let {
                        it should haveSize(1)
                        it.first().let { (account, domain) ->
                            account should be(account)
                            domain should be(Domain(DOMAIN_SAMPLE_COM))
                        }
                    }

                    localRepository.cache.let {
                        it should haveSize(1)
                        it.first().id should be(account.id.value)
                    }
                }
            }

            context("and current domain is not unknown") {
                val localRepository = MockLocalAuthorizedAccountRepository(
                    account = null,
                    domain = null,
                )
                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                    localRepository,
                )

                it("raises AuthorizedAccountNotFoundException") {
                    val instances: MutableList<Pair<Account, Domain>> = mutableListOf()

                    shouldThrowExactly<AuthorizedTokenNotFoundException> {
                        runBlocking { useCase.execute().collect { instances.add(it) } }
                    }
                    instances should haveSize(0)
                    localRepository.cache should haveSize(0)
                }
            }

            context("and the sever returns error response") {
                val localRepository = MockLocalAuthorizedAccountRepository(
                    account = null,
                    domain = Domain(DOMAIN_SAMPLE_COM),
                )
                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    HttpStatusCode.BadRequest,
                    localRepository,
                )

                it("raises AuthorizedAccountNotFoundException") {
                    val instances: MutableList<Pair<Account, Domain>> = mutableListOf()

                    shouldThrowExactly<AuthorizedTokenNotFoundException> {
                        runBlocking { useCase.execute().collect { instances.add(it) } }
                    }
                    instances should haveSize(0)
                    localRepository.cache should haveSize(0)
                }
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

            context("and the sever returns valid response") {
                val localRepository = MockLocalAuthorizedAccountRepository(
                    current = current,
                    accessToken = { DUMMY_ACCESS_TOKEN to Domain(DOMAIN_SAMPLE_COM) },
                )

                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                    localRepository,
                )

                it("returns flow with two instances of account") {
                    runBlocking { useCase.execute().toList() }.let {
                        it should haveSize(2)
                        it.forEach { (account, domain) ->
                            account should be(account)
                            domain should be(Domain(DOMAIN_SAMPLE_COM))
                        }
                    }

                    localRepository.cache.let {
                        it should haveSize(1)
                        it.first().id should be(account.id.value)
                    }
                }
            }
            context("and the sever returns error response") {
                val localRepository = MockLocalAuthorizedAccountRepository(
                    current = current,
                    accessToken = { DUMMY_ACCESS_TOKEN to Domain(DOMAIN_SAMPLE_COM) },
                )

                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    HttpStatusCode.BadRequest,
                    localRepository,
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

                    localRepository.cache should haveSize(0)
                }
            }
        }
    }
})

private inline fun <reified T> buildUseCase(
    resource: T,
    expected: String,
    localRepository: MockLocalAuthorizedAccountRepository,
): FetchCurrentAuthorizedAccountUseCase = TestTimelineUseCaseComponent(
    MockHttpClientEngine(resource, expected),
    localRepository,
).scope.get()

private inline fun <reified T> buildUseCase(
    resource: T,
    errorStatusCode: HttpStatusCode,
    localRepository: MockLocalAuthorizedAccountRepository,
): FetchCurrentAuthorizedAccountUseCase = TestTimelineUseCaseComponent(
    MockHttpClientEngine(resource, errorStatusCode),
    localRepository,
).scope.get()
