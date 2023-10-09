package noctiluca.authentication.domain.usecase.spec

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import noctiluca.authentication.domain.TestAuthenticationUseCaseComponent
import noctiluca.authentication.domain.mock.MockLocalTokenRepository
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.json.*
import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.network.authentication.OAuth
import noctiluca.network.authentication.json.AppCredentialJson
import noctiluca.network.mastodon.Api
import noctiluca.test.ACCOUNT_ID
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.JSON_ACCOUNT_CREDENTIAL
import noctiluca.test.mock.MockHttpClientEngine

class RequestAccessTokenUseCaseSpec : DescribeSpec({
    val json = AppCredentialJson(TEST_CLIENT_ID, TEST_CLIENT_SECRET)

    describe("#execute") {
        context("when the local cache does not exist") {
            val localRepository = MockLocalTokenRepository()
            val testCase = buildUseCase(
                MockHttpClientEngine
                    .mock(OAuth.Token(), JSON_OAUTH_TOKEN)
                    .mock(Api.V1.Accounts.VerifyCredentials(), JSON_ACCOUNT_CREDENTIAL)
                    .build(),
                localRepository,
            )

            it("returns null") {
                runBlocking {
                    testCase.execute(
                        TEST_CODE,
                        Uri(TEST_REDIRECT_URL),
                    )
                } should beNull()
            }
        }

        context("when the local cache exists") {
            context("and the server returns valid response") {
                val localRepository = MockLocalTokenRepository(
                    currentAppCredential = Domain(DOMAIN_SAMPLE_COM) to json
                )
                val testCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(OAuth.Token(), JSON_OAUTH_TOKEN)
                        .mock(Api.V1.Accounts.VerifyCredentials(), JSON_ACCOUNT_CREDENTIAL)
                        .build(),
                    localRepository,
                )

                it("returns AuthorizedUser") {
                    runBlocking {
                        testCase.execute(
                            TEST_CODE,
                            Uri(TEST_REDIRECT_URL),
                        )
                    }!!.also {
                        it.id should be(AccountId(ACCOUNT_ID))
                        it.domain should be(Domain(DOMAIN_SAMPLE_COM))
                    }

                    localRepository.credentials should beEmpty()
                    localRepository.users.let {
                        it should haveSize(1)
                        it.first().id should be(AccountId(ACCOUNT_ID))
                        it.first().domain should be(Domain(DOMAIN_SAMPLE_COM))
                    }
                }
            }
        }

        context("when the server returns error response") {
            val localRepository = MockLocalTokenRepository(
                currentAppCredential = Domain(DOMAIN_SAMPLE_COM) to json
            )
            val testCase = buildUseCase(
                MockHttpClientEngine
                    .mock(OAuth.Token(), HttpStatusCode.BadRequest)
                    .mock(Api.V1.Accounts.VerifyCredentials(), HttpStatusCode.BadRequest)
                    .build(),
                localRepository,
            )

            it("raises ClientRequestException") {
                shouldThrowExactly<ClientRequestException> {
                    runBlocking {
                        testCase.execute(
                            TEST_CODE,
                            Uri(TEST_REDIRECT_URL),
                        )
                    }
                }

                localRepository.credentials should beEmpty()
                localRepository.users should beEmpty()
            }
        }
    }
})

private fun buildUseCase(
    engine: MockEngine,
    localRepository: MockLocalTokenRepository,
): RequestAccessTokenUseCase = TestAuthenticationUseCaseComponent(
    engine,
    localRepository,
).scope.get()
