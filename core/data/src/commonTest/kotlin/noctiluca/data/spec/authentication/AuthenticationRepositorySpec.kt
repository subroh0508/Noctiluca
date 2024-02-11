package noctiluca.data.spec.authentication

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.containExactly
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import noctiluca.data.TestDataComponent
import noctiluca.data.authentication.AuthenticationRepository
import noctiluca.data.authentication.impl.AuthenticationRepositoryImpl
import noctiluca.data.json.authentication.*
import noctiluca.data.mock.MockAppCredentialDataStore
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.*
import noctiluca.model.authentication.AppCredential
import noctiluca.network.authentication.Api
import noctiluca.network.authentication.OAuth
import noctiluca.network.authentication.di.AuthenticationApiModule
import noctiluca.network.instancessocial.di.buildHttpClient
import noctiluca.test.ACCOUNT_ID
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.JSON_ACCOUNT_CREDENTIAL
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.test.mock.buildEmptyMockAuthenticationTokenDataStore
import noctiluca.test.mock.buildFilledMockAuthenticationTokenDataStore
import org.koin.core.component.get

class AuthenticationRepositorySpec : DescribeSpec({
    coroutineTestScope = true

    describe("#fetchAuthorizeUrl") {
        context("when the server returns valid response") {
            val mockAppCredentialDataStore = MockAppCredentialDataStore()
            val repository = buildRepository(
                MockHttpClientEngine
                    .mock(Api.V1.Apps(), JSON_APP_CREDENTIAL)
                    .build(),
                mockAppCredentialDataStore,
            )

            it("returns URL for authorize") {
                runBlocking {
                    repository.fetchAuthorizeUrl(
                        Domain(DOMAIN_SAMPLE_COM),
                        TEST_CLIENT_ID,
                        Uri(TEST_REDIRECT_URL),
                    )
                } should be(buildAuthorizedUrl())

                mockAppCredentialDataStore.getCurrent()!!.let {
                    it.clientId should be(TEST_CLIENT_ID)
                    it.clientSecret should be(TEST_CLIENT_SECRET)
                }
            }
        }

        context("when the server returns invalid response") {
            val mockAppCredentialDataStore = MockAppCredentialDataStore()
            val repository = buildRepository(
                MockHttpClientEngine
                    .mock(Api.V1.Apps(), HttpStatusCode.BadRequest)
                    .build(),
                mockAppCredentialDataStore,
            )

            it("raises HttpException") {
                shouldThrowExactly<HttpException> {
                    repository.fetchAuthorizeUrl(
                        Domain(DOMAIN_SAMPLE_COM),
                        TEST_CLIENT_ID,
                        Uri(TEST_REDIRECT_URL),
                    )
                }

                mockAppCredentialDataStore.getCurrent() should beNull()
            }
        }
    }

    describe("#fetchAccessToken") {
        context("when the local cache does not exist") {
            val repository = buildRepository(
                MockHttpClientEngine
                    .mock(OAuth.Token(), JSON_OAUTH_TOKEN)
                    .mock(Api.V1.Accounts.VerifyCredentials(), JSON_ACCOUNT_CREDENTIAL)
                    .build(),
            )

            it("returns the authorized user") {
                runBlocking {
                    repository.fetchAccessToken(TEST_CODE, Uri(TEST_REDIRECT_URL))
                } should beNull()
            }
        }

        context("when the server returns valid response") {
            val mockAuthenticationTokenDataStore = buildEmptyMockAuthenticationTokenDataStore()
            val repository = buildRepository(
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
                mockAuthenticationTokenDataStore,
            )

            it("returns AuthorizedUser") {
                runBlocking {
                    repository.fetchAccessToken(TEST_CODE, Uri(TEST_REDIRECT_URL))
                }?.id should be(AccountId(ACCOUNT_ID))

                mockAuthenticationTokenDataStore.getAll().let {
                    it should haveSize(1)
                    it.first().id should be(AccountId(ACCOUNT_ID))
                    it.first().domain should be(Domain(DOMAIN_SAMPLE_COM))
                }
            }
        }

        context("when the server returns invalid response") {
            val mockAuthenticationTokenDataStore = buildEmptyMockAuthenticationTokenDataStore()
            val repository = buildRepository(
                MockHttpClientEngine
                    .mock(OAuth.Token(), HttpStatusCode.BadRequest)
                    .build(),
                MockAppCredentialDataStore(
                    AppCredential(
                        TEST_CLIENT_ID,
                        TEST_CLIENT_SECRET,
                        Domain(DOMAIN_SAMPLE_COM),
                        Uri("dummy"),
                    ),
                ),
                mockAuthenticationTokenDataStore,
            )

            it("raises HttpException") {
                shouldThrowExactly<HttpException> {
                    runBlocking {
                        repository.fetchAccessToken(TEST_CODE, Uri(TEST_REDIRECT_URL))
                    }
                }

                mockAuthenticationTokenDataStore.getAll() should haveSize(0)
            }
        }
    }

    describe("#switchAccessToken") {
        context("when the local cache does not exist") {
            val repository = buildRepository(
                MockHttpClientEngine
                    .mock(Api.V1.Accounts.VerifyCredentials(), JSON_ACCOUNT_CREDENTIAL)
                    .build(),
            )

            it("raises NoSuchElementException") {
                shouldThrowExactly<NoSuchElementException> {
                    runBlocking {
                        repository.switchAccessToken(AccountId(ACCOUNT_ID))
                    }
                }
            }
        }

        context("when the local cache exists") {
            val mockAuthenticationTokenDataStore = buildFilledMockAuthenticationTokenDataStore(
                init = listOf(object : AuthorizedUser {
                    override val id = AccountId("100")
                    override val domain = Domain("hoge.com")
                }),
                currentAccessToken = "yyy",
            )
            val repository = buildRepository(
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
                mockAuthenticationTokenDataStore,
            )

            it("returns the authorized user") {
                runBlocking {
                    repository.fetchAccessToken(TEST_CODE, Uri(TEST_REDIRECT_URL))
                    repository.switchAccessToken(AccountId(ACCOUNT_ID))
                }.id should be(AccountId(ACCOUNT_ID))

                runBlocking {
                    mockAuthenticationTokenDataStore.getAll()
                }.map(AuthorizedUser::id) should containExactly(
                    AccountId(ACCOUNT_ID),
                    AccountId("100"),
                )
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

private fun buildRepository(
    mockEngine: MockEngine,
    mockAppCredentialDataStore: AppCredentialDataStore = MockAppCredentialDataStore(),
    mockAuthenticationTokenDataStore: AuthenticationTokenDataStore = buildEmptyMockAuthenticationTokenDataStore(),
): AuthenticationRepository {
    val json = Json {
        explicitNulls = false
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    val component = TestDataComponent(
        mockEngine,
        mockAuthenticationTokenDataStore,
    ) {
        AuthenticationApiModule(buildHttpClient(json, mockEngine))
        single { mockAppCredentialDataStore }
    }

    return AuthenticationRepositoryImpl(
        component.get(),
        component.get(),
        component.get(),
    )
}
