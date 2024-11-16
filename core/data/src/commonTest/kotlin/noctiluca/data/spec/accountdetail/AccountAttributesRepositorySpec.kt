package noctiluca.data.spec.accountdetail

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.engine.runBlocking
import io.kotest.matchers.collections.containExactly
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.flow.first
import noctiluca.data.TestDataComponent
import noctiluca.data.accountdetail.AccountAttributesRepository
import noctiluca.data.accountdetail.impl.AccountAttributesRepositoryImpl
import noctiluca.data.json.*
import noctiluca.model.HttpException
import noctiluca.model.HttpUnauthorizedException
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.network.mastodon.Api
import noctiluca.test.shared.ACCOUNT_ID
import noctiluca.test.shared.extension.flowToList
import noctiluca.test.shared.mock.MockAuthorizationTokenDataStore
import noctiluca.test.shared.mock.MockHttpClientEngine
import noctiluca.test.shared.mock.buildFilledMockAuthenticationTokenDataStore
import org.koin.core.component.get

class AccountAttributesRepositorySpec : DescribeSpec({
    coroutineTestScope = true

    describe("#attributes") {
        context("when the server returns valid response") {
            context("and the id is mine") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(Api.V1.Accounts.Id(id = ACCOUNT_ID), JSON_MY_ACCOUNT)
                        .build(),
                )

                it("returns the account detail") {
                    flowToList(
                        repository.attributes(myAccount.id),
                    ) should containExactly(
                        myAccount,
                    )
                }
            }

            context("and the id is not mine") {
                eachAccountCondition { json, name, condition ->
                    context("and the $name") {
                        val repository = buildRepository(
                            MockHttpClientEngine
                                .mock(Api.V1.Accounts.Id(id = otherAccount.id.value), json)
                                .build(),
                        )

                        it("returns the account detail") {
                            flowToList(
                                repository.attributes(otherAccount.id),
                            ) should containExactly(
                                otherAccount.copy(condition = condition),
                            )
                        }
                    }
                }
            }
        }

        context("when the server returns invalid response") {
            context("and the id is mine") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Id(id = myAccount.id.value),
                            HttpStatusCode.Unauthorized,
                        )
                        .build(),
                )

                it("raises HttpUnauthorizedException") {
                    shouldThrowExactly<HttpUnauthorizedException> {
                        runBlocking { repository.attributes(myAccount.id).first() }
                    }
                }
            }
            context("and the id is not mine") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Id(id = otherAccount.id.value),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("returns the account detail with none relationships") {
                    shouldThrowExactly<HttpException> {
                        runBlocking { repository.attributes(otherAccount.id).first() }
                    }
                }
            }
        }
    }
})

private inline fun eachAccountCondition(
    block: (String, String, AccountAttributes.Condition?) -> Unit,
) = listOf(
    JSON_OTHER_ACCOUNT,
    JSON_LIMITED_ACCOUNT,
    JSON_SUSPENDED_ACCOUNT,
).forEach { json ->
    val (name, condition) = when (json) {
        JSON_OTHER_ACCOUNT -> "normal account" to null
        JSON_LIMITED_ACCOUNT -> "account limited" to AccountAttributes.Condition.LIMITED
        JSON_SUSPENDED_ACCOUNT -> "account suspended" to AccountAttributes.Condition.SUSPENDED
        else -> error("invalid json: $json")
    }

    block(json, name, condition)
}

private fun buildRepository(
    mockEngine: MockEngine,
    mockAuthenticationTokenDataStore: MockAuthorizationTokenDataStore = buildFilledMockAuthenticationTokenDataStore(),
): AccountAttributesRepository {
    val component = TestDataComponent(
        mockEngine,
        mockAuthenticationTokenDataStore,
    )

    return AccountAttributesRepositoryImpl(
        component.get(),
    )
}
