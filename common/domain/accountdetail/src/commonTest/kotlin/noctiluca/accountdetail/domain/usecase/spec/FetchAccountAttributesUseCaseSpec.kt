package noctiluca.accountdetail.domain.usecase.spec

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import noctiluca.accountdetail.domain.TestAccountDetailUseCaseComponent
import noctiluca.accountdetail.domain.myAccount
import noctiluca.accountdetail.domain.otherAccount
import noctiluca.accountdetail.domain.usecase.FetchAccountAttributesUseCase
import noctiluca.accountdetail.domain.usecase.json.*
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.accountdetail.model.Relationship
import noctiluca.accountdetail.model.Relationships
import noctiluca.api.mastodon.Api
import noctiluca.model.AccountId
import noctiluca.test.ACCOUNT_ID
import noctiluca.test.mock.MockHttpClientEngine

class FetchAccountAttributesUseCaseSpec : DescribeSpec({
    describe("#execute") {
        context("when the server returns valid response") {
            context("and the id is mine") {
                val testCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(Api.V1.Accounts.Id(id = ACCOUNT_ID), JSON_MY_ACCOUNT)
                        .build(),
                )

                it("returns the account detail") {
                    runBlocking {
                        testCase.execute(AccountId(ACCOUNT_ID))
                    } should be(myAccount)
                }
            }

            context("and the id is not mine") {
                eachAccountCondition { json, name, condition ->
                    context("and the $name") {
                        val testCase = buildUseCase(
                            MockHttpClientEngine
                                .mock(Api.V1.Accounts.Id(id = OTHER_ACCOUNT_ID), json)
                                .mock(Api.V1.Accounts.Relationships(), "[$JSON_ACCOUNTS_RELATIONSHIP_NONE]")
                                .build(),
                        )

                        it("returns the account detail") {
                            runBlocking {
                                testCase.execute(AccountId(OTHER_ACCOUNT_ID))
                            } should be(otherAccount.copy(condition = condition))
                        }
                    }
                }

                eachRelationship { json, relationship ->
                    context("and the ${relationship.name.lowercase()} is true") {
                        val testCase = buildUseCase(
                            MockHttpClientEngine
                                .mock(Api.V1.Accounts.Id(id = OTHER_ACCOUNT_ID), JSON_OTHER_ACCOUNT)
                                .mock(Api.V1.Accounts.Relationships(), "[$json]")
                                .build(),
                        )

                        it("returns the account detail") {
                            runBlocking {
                                testCase.execute(AccountId(OTHER_ACCOUNT_ID))
                            } should be(otherAccount.copy(relationships = Relationships(relationship)))
                        }
                    }
                }
            }
        }

        context("when the server returns invalid response") {
            context("and the id is mine") {
                val testCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(Api.V1.Accounts.Id(id = ACCOUNT_ID), HttpStatusCode.Unauthorized)
                        .build(),
                )

                it("raises ClientRequestException") {
                    shouldThrowExactly<ClientRequestException> {
                        runBlocking {
                            testCase.execute(AccountId(ACCOUNT_ID))
                        }
                    }
                }
            }
            context("and the id is not mine") {
                val testCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(Api.V1.Accounts.Id(id = OTHER_ACCOUNT_ID), JSON_OTHER_ACCOUNT)
                        .mock(Api.V1.Accounts.Relationships(), HttpStatusCode.UnprocessableEntity)
                        .build(),
                )

                it("returns the account detail with none relationships") {
                    runBlocking {
                        testCase.execute(AccountId(OTHER_ACCOUNT_ID))
                    } should be(otherAccount)
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

private fun buildUseCase(
    engine: MockEngine,
): FetchAccountAttributesUseCase = TestAccountDetailUseCaseComponent(
    engine,
).scope.get()
