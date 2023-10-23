package noctiluca.authentication.domain.usecase.spec

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import noctiluca.authentication.domain.TestAuthenticationUseCaseComponent
import noctiluca.authentication.domain.mock.MockAppCredentialDataStore
import noctiluca.authentication.domain.mock.MockTokenDataStore
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.authentication.domain.usecase.json.*
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.network.authentication.Api
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.mock.MockHttpClientEngine

class RequestAppCredentialUseCaseSpec : DescribeSpec({
    describe("#execute") {
        context("when the server returns valid response") {
            val mockAppCredentialDataStore = MockAppCredentialDataStore()
            val useCase = buildAuthenticationUseCase(
                MockHttpClientEngine
                    .mock(Api.V1.Apps(), JSON_APP_CREDENTIAL)
                    .build(),
                mockAppCredentialDataStore,
            )

            it("returns URL for authorize") {
                runBlocking {
                    useCase.execute(
                        Domain(DOMAIN_SAMPLE_COM),
                        TEST_CLIENT_NAME,
                        Uri(TEST_REDIRECT_URL),
                    )
                } should be(buildAuthorizedUrl())

                mockAppCredentialDataStore.getCurrent()!!.let {
                    it.clientId should be(TEST_CLIENT_ID)
                    it.clientSecret should be(TEST_CLIENT_SECRET)
                }
            }
        }

        context("when the server returns error response") {
            val mockAppCredentialDataStore = MockAppCredentialDataStore()
            val useCase = buildAuthenticationUseCase(
                MockHttpClientEngine
                    .mock(Api.V1.Apps(), HttpStatusCode.BadRequest)
                    .build(),
                mockAppCredentialDataStore,
            )

            it("raises ClientRequestException") {
                shouldThrowExactly<ClientRequestException> {
                    useCase.execute(
                        Domain(DOMAIN_SAMPLE_COM),
                        TEST_CLIENT_NAME,
                        Uri(TEST_REDIRECT_URL),
                    )
                }
            }
        }
    }
})

private fun buildAuthorizedUrl() = buildString {
    append("${URLProtocol.HTTPS.name}://$DOMAIN_SAMPLE_COM/oauth/authorize")
    append("?response_type=code")
    append("&client_id=$TEST_CLIENT_ID")
    append("&redirect_uri=$TEST_ENCODED_REDIRECT_URL")
    append("&scope=read+write+follow+push")
}.let { Uri(it) }

private fun buildAuthenticationUseCase(
    engine: MockEngine,
    mockAppCredentialDataStore: AppCredentialDataStore = MockAppCredentialDataStore(),
): RequestAppCredentialUseCase = TestAuthenticationUseCaseComponent(
    engine,
    mockAppCredentialDataStore,
    MockTokenDataStore(),
).scope.get()
