package noctiluca.timeline.domain.usecase.spec

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import noctiluca.account.infra.repository.local.LocalAccountCredentialCache
import noctiluca.account.model.Account
import noctiluca.api.mastodon.Api
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.test.DOMAIN_SAMPLE_COM
import noctiluca.test.URL_SAMPLE_COM
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.timeline.domain.TestTimelineUseCaseComponent
import noctiluca.timeline.domain.usecase.FetchCurrentAuthorizedAccountUseCase
import noctiluca.timeline.domain.usecase.json.JSON_ACCOUNT_CREDENTIAL_1

class FetchCurrentAuthorizedAccountUseCaseSpec : DescribeSpec({
    val account = Account(
        AccountId("1"),
        "test1",
        "テスト太郎",
        Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
        "@test1@$DOMAIN_SAMPLE_COM",
    )
    val accounts = listOf(
        Account(
            AccountId("1"),
            "test1",
            "テスト太郎",
            Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
            "@test1@$DOMAIN_SAMPLE_COM",
        ),
        Account(
            AccountId("2"),
            "test2",
            "テスト次郎",
            Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
            "@test2@$DOMAIN_SAMPLE_COM",
        ),
        Account(
            AccountId("3"),
            "test3",
            "テスト三郎",
            Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
            "@test3@$DOMAIN_SAMPLE_COM",
        ),
    )

    describe("#execute") {
        context("when the local cache is empty") {
            it("returns flow with one instance of account") {
                val localCache = MockLocalCache()
                val useCase = buildUseCase(
                    Api.V1.Accounts.VerifyCredentials(),
                    JSON_ACCOUNT_CREDENTIAL_1,
                    localCache,
                )

                runBlocking { useCase.execute().toList() }.let {
                    it should haveSize(1)
                    it.first().let { (account, domain) ->
                        account should be(account)
                        domain should be(Domain(URL_SAMPLE_COM))
                    }
                }

                localCache.json.first().id should be(account.id.value)
            }
        }
    }
})

private class MockLocalCache : LocalAccountCredentialCache {
    private val cache: MutableList<AccountCredentialJson> = mutableListOf()

    val json: List<AccountCredentialJson> get() = cache

    override suspend fun get(id: AccountId) = cache.find { it.id == id.value }
    override suspend fun add(json: AccountCredentialJson): List<AccountCredentialJson> {
        cache.add(json)

        return cache
    }

    override suspend fun delete(id: AccountId): List<AccountCredentialJson> {
        cache.removeIf { it.id == id.value }

        return cache
    }
}

private inline fun <reified T> buildUseCase(
    resource: T,
    expected: String,
    localCache: MockLocalCache,
): FetchCurrentAuthorizedAccountUseCase = TestTimelineUseCaseComponent(
    MockHttpClientEngine(resource, expected),
    localCache,
).scope.get()
