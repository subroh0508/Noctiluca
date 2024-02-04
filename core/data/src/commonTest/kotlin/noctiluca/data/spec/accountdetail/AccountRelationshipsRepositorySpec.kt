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
import noctiluca.data.accountdetail.AccountRelationshipsRepository
import noctiluca.data.accountdetail.impl.AccountRelationshipsRepositoryImpl
import noctiluca.data.json.*
import noctiluca.model.HttpException
import noctiluca.model.accountdetail.Relationship
import noctiluca.model.accountdetail.Relationships
import noctiluca.network.mastodon.Api
import noctiluca.test.extension.flowToList
import noctiluca.test.mock.MockAuthenticationTokenDataStore
import noctiluca.test.mock.MockHttpClientEngine
import noctiluca.test.mock.buildFilledMockAuthenticationTokenDataStore
import org.koin.core.component.get

class AccountRelationshipsRepositorySpec : DescribeSpec({
    coroutineTestScope = true

    describe("#relationships") {
        context("when the server returns valid response") {
            context("and the id is mine") {
                val repository = buildRepository(
                    MockHttpClientEngine.Builder()
                        .build(),
                )

                it("returns Relationships.ME") {
                    flowToList(
                        repository.relationships(myAccount.id),
                    ) should containExactly(
                        Relationships.ME,
                    )
                }
            }

            context("and the id is not mine") {
                eachRelationship { json, relationship ->
                    context("and the ${relationship.name.lowercase()} is true") {
                        val repository = buildRepository(
                            MockHttpClientEngine
                                .mock(Api.V1.Accounts.Relationships(), "[$json]")
                                .build(),
                        )

                        it("returns the account detail") {
                            flowToList(
                                repository.relationships(otherAccount.id),
                            ) should containExactly(
                                Relationships(relationship),
                            )
                        }
                    }
                }
            }
        }

        context("when the server returns invalid response") {
            context("and the id is not mine") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("returns the account detail with none relationships") {
                    shouldThrowExactly<HttpException> {
                        runBlocking { repository.relationships(otherAccount.id).first() }
                    }
                }
            }
        }
    }
})

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
): AccountRelationshipsRepository {
    val component = TestDataComponent(
        mockEngine,
        mockAuthenticationTokenDataStore,
    )

    return AccountRelationshipsRepositoryImpl(
        component.get(),
        component.get(),
    )
}
