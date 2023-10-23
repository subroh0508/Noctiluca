package noctiluca.authentication.domain.usecase.spec

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import noctiluca.authentication.domain.TestAuthenticationUseCaseComponent
import noctiluca.authentication.domain.mock.MockAppCredentialDataStore
import noctiluca.authentication.domain.mock.MockTokenDataStore
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.json.*
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.TokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.authentication.AppCredential
import noctiluca.network.authentication.OAuth
import noctiluca.network.mastodon.Api
import noctiluca.test.ACCOUNT_ID
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.JSON_ACCOUNT_CREDENTIAL
import noctiluca.test.mock.MockHttpClientEngine

class RequestAccessTokenUseCaseSpec : DescribeSpec({
    describe("#execute") {
        context("when the local cache does not exist") {
            val testCase = buildAuthenticationUseCase(
                MockHttpClientEngine
                    .mock(OAuth.Token(), JSON_OAUTH_TOKEN)
                    .mock(Api.V1.Accounts.VerifyCredentials(), JSON_ACCOUNT_CREDENTIAL)
                    .build(),
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
                val mockTokenDataStore = MockTokenDataStore()
                val testCase = buildAuthenticationUseCase(
                    MockHttpClientEngine
                        .mock(OAuth.Token(), JSON_OAUTH_TOKEN)
                        .mock(Api.V1.Accounts.VerifyCredentials(), JSON_ACCOUNT_CREDENTIAL)
                        .build(),
                    MockAppCredentialDataStore(
                        AppCredential(
                            TEST_CLIENT_ID,
                            TEST_CLIENT_SECRET,
                            Domain(DOMAIN_SAMPLE_COM),
                            Uri("dummy"),
                        ),
                    ),
                    mockTokenDataStore,
                )

                it("returns AuthorizedUser") {
                    runBlocking {
                        testCase.execute(
                            TEST_CODE,
                            Uri(TEST_REDIRECT_URL),
                        )
                    } shouldNot beNull()

                    mockTokenDataStore.getAll().let {
                        it should haveSize(1)
                        it.first().id should be(AccountId(ACCOUNT_ID))
                        it.first().domain should be(Domain(DOMAIN_SAMPLE_COM))
                    }
                }
            }
        }

        context("when the server returns error response") {
            val mockTokenDataStore = MockTokenDataStore()
            val testCase = buildAuthenticationUseCase(
                MockHttpClientEngine
                    .mock(OAuth.Token(), HttpStatusCode.BadRequest)
                    .mock(Api.V1.Accounts.VerifyCredentials(), HttpStatusCode.BadRequest)
                    .build(),
                MockAppCredentialDataStore(
                    AppCredential(
                        TEST_CLIENT_ID,
                        TEST_CLIENT_SECRET,
                        Domain(DOMAIN_SAMPLE_COM),
                        Uri("dummy"),
                    ),
                ),
                mockTokenDataStore,
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

                mockTokenDataStore.getAll() should beEmpty()
            }
        }
    }
})

private fun buildAuthenticationUseCase(
    engine: MockEngine,
    mockAppCredentialDataStore: AppCredentialDataStore = MockAppCredentialDataStore(),
    mockTokenDataStore: TokenDataStore = MockTokenDataStore(),
): RequestAccessTokenUseCase = TestAuthenticationUseCaseComponent(
    engine,
    mockAppCredentialDataStore,
    mockTokenDataStore,
).scope.get()
