package noctiluca.data.spec.accountdetail

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.containExactly
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.flow.first
import noctiluca.data.TestDataComponent
import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.accountdetail.impl.AccountDetailRepositoryImpl
import noctiluca.data.json.*
import noctiluca.model.HttpException
import noctiluca.model.HttpUnauthorizedException
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationship
import noctiluca.model.accountdetail.Relationships
import noctiluca.network.mastodon.Api
import noctiluca.test.ACCOUNT_ID
import noctiluca.test.extension.flowToList
import noctiluca.test.mock.MockAuthenticationTokenDataStore
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.test.mock.buildFilledMockAuthenticationTokenDataStore
import org.koin.core.component.get

class AccountDetailRepositorySpec : DescribeSpec({
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
                                .mock(
                                    Api.V1.Accounts.Relationships(),
                                    "[$JSON_ACCOUNTS_RELATIONSHIP_NONE]",
                                )
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

                eachRelationship { json, relationship ->
                    context("and the ${relationship.name.lowercase()} is true") {
                        val repository = buildRepository(
                            MockHttpClientEngine
                                .mock(
                                    Api.V1.Accounts.Id(id = otherAccount.id.value),
                                    JSON_OTHER_ACCOUNT,
                                )
                                .mock(Api.V1.Accounts.Relationships(), "[$json]")
                                .build(),
                        )

                        it("returns the account detail") {
                            flowToList(
                                repository.attributes(otherAccount.id),
                            ) should containExactly(
                                otherAccount,
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

private inline fun eachRelationship(
    block: (String, Relationship) -> Unit,
) = listOf(
    JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING,
    JSON_ACCOUNTS_RELATIONSHIP_SHOWING_REBLOGS,
    JSON_ACCOUNTS_RELATIONSHIP_NOTIFYING,
    JSON_ACCOUNTS_RELATIONSHIP_FOLLOWED_BY,
    JSON_ACCOUNTS_RELATIONSHIP_BLOCKING,
    JSON_ACCOUNTS_RELATIONSHIP_BLOCKED_BY,
    JSON_ACCOUNTS_RELATIONSHIP_MUTING,
    JSON_ACCOUNTS_RELATIONSHIP_MUTING_NOTIFICATIONS,
    JSON_ACCOUNTS_RELATIONSHIP_REQUESTED,
    JSON_ACCOUNTS_RELATIONSHIP_DOMAIN_BLOCKING,
    JSON_ACCOUNTS_RELATIONSHIP_ENDORSED,
).forEach { json ->
    val relationship = when (json) {
        JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING -> Relationship.FOLLOWING
        JSON_ACCOUNTS_RELATIONSHIP_SHOWING_REBLOGS -> Relationship.SHOWING_REBLOGS
        JSON_ACCOUNTS_RELATIONSHIP_NOTIFYING -> Relationship.NOTIFYING
        JSON_ACCOUNTS_RELATIONSHIP_FOLLOWED_BY -> Relationship.FOLLOWED_BY
        JSON_ACCOUNTS_RELATIONSHIP_BLOCKING -> Relationship.BLOCKING
        JSON_ACCOUNTS_RELATIONSHIP_BLOCKED_BY -> Relationship.BLOCKED_BY
        JSON_ACCOUNTS_RELATIONSHIP_MUTING -> Relationship.MUTING
        JSON_ACCOUNTS_RELATIONSHIP_MUTING_NOTIFICATIONS -> Relationship.MUTING_NOTIFICATIONS
        JSON_ACCOUNTS_RELATIONSHIP_REQUESTED -> Relationship.REQUESTED
        JSON_ACCOUNTS_RELATIONSHIP_DOMAIN_BLOCKING -> Relationship.DOMAIN_BLOCKING
        JSON_ACCOUNTS_RELATIONSHIP_ENDORSED -> Relationship.ENDORSED
        else -> error("invalid json: $json")
    }

    block(json, relationship)
}

private fun buildRepository(
    mockEngine: MockEngine,
    mockAuthenticationTokenDataStore: MockAuthenticationTokenDataStore = buildFilledMockAuthenticationTokenDataStore(),
): AccountDetailRepository {
    val component = TestDataComponent(
        mockEngine,
        mockAuthenticationTokenDataStore,
    )

    return AccountDetailRepositoryImpl(
        component.get(),
    )
}
