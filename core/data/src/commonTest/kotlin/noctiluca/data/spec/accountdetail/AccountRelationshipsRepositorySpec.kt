package noctiluca.data.spec.accountdetail

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.containExactly
import io.kotest.matchers.collections.haveSize
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
                    MockHttpClientEngine
                        .Builder()
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
                    context("and the ${relationship?.name?.lowercase() ?: "none"} is true") {
                        val repository = buildRepository(
                            MockHttpClientEngine
                                .mock(Api.V1.Accounts.Relationships(), "[$json]")
                                .build(),
                        )

                        it("returns the account detail") {
                            flowToList(
                                repository.relationships(otherAccount.id),
                            ) should containExactly(
                                Relationships(setOfNotNull(relationship)),
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

    describe("#follow") {
        context("when the server returns valid response") {
            context("and the id is mine") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .Builder()
                        .build(),
                )

                it("returns Relationships.ME") {
                    val instances = flowToList(repository.relationships(myAccount.id))

                    runBlocking {
                        repository.follow(myAccount.id)

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships.ME,
                        )
                    }
                }
            }

            context("and the id is not following") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_NONE]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Follow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING,
                        )
                        .build(),
                )

                it("changes Relationships.NONE -> Relationships.FOLLOWING") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        repository.follow(otherAccount.id)

                        instances should haveSize(2)
                        instances should containExactly(
                            Relationships.NONE,
                            Relationships(Relationship.FOLLOWING),
                        )
                    }
                }
            }

            context("and the id is following") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Unfollow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            JSON_ACCOUNTS_RELATIONSHIP_NONE,
                        )
                        .build(),
                )

                it("changes Relationships.FOLLOWING -> Relationships.NONE") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        repository.follow(otherAccount.id)

                        instances should haveSize(2)
                        instances should containExactly(
                            Relationships(Relationship.FOLLOWING),
                            Relationships.NONE,
                        )
                    }
                }
            }

            filterRelationship(
                Relationship.BLOCKING,
                Relationship.BLOCKED_BY,
            ) { json, relationship ->
                context("and the id is ${relationship.name.lowercase()}") {
                    val repository = buildRepository(
                        MockHttpClientEngine
                            .mock(
                                Api.V1.Accounts.Relationships(),
                                "[$json]",
                            )
                            .build(),
                    )

                    it("returns Relationship.${relationship.name}") {
                        val instances = flowToList(repository.relationships(otherAccount.id))

                        runBlocking {
                            repository.follow(otherAccount.id)

                            instances should haveSize(1)
                            instances should containExactly(
                                Relationships(relationship),
                            )
                        }
                    }
                }
            }
        }
        context("when the server returns invalid response") {
            context("and the id is not followed") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_NONE]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Follow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.follow(otherAccount.id)
                        }

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships.NONE,
                        )
                    }
                }
            }

            context("and the id is following") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Unfollow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.follow(otherAccount.id)
                        }

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships(Relationship.FOLLOWING),
                        )
                    }
                }
            }
        }
    }

    describe("#block") {
        context("when the server returns valid response") {
            context("and the id is mine") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .Builder()
                        .build(),
                )

                it("returns Relationships.ME") {
                    val instances = flowToList(repository.relationships(myAccount.id))

                    runBlocking {
                        repository.block(myAccount.id)

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships.ME,
                        )
                    }
                }
            }

            context("and the id is not blocking") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_NONE]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Block(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            JSON_ACCOUNTS_RELATIONSHIP_BLOCKING,
                        )
                        .build(),
                )

                it("changes Relationships.NONE -> Relationships.BLOCKING") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        repository.block(otherAccount.id)

                        instances should haveSize(2)
                        instances should containExactly(
                            Relationships.NONE,
                            Relationships(Relationship.BLOCKING),
                        )
                    }
                }
            }

            context("and the id is blocking") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_BLOCKING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Unblock(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            JSON_ACCOUNTS_RELATIONSHIP_NONE,
                        )
                        .build(),
                )

                it("changes Relationships.BLOCKING -> Relationships.NONE") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        repository.block(otherAccount.id)

                        instances should haveSize(2)
                        instances should containExactly(
                            Relationships(Relationship.BLOCKING),
                            Relationships.NONE,
                        )
                    }
                }
            }
        }
        context("when the server returns invalid response") {
            context("and the id is not blocking") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_NONE]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Block(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.block(otherAccount.id)
                        }

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships.NONE,
                        )
                    }
                }
            }

            context("and the id is blocking") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_BLOCKING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Unblock(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.block(otherAccount.id)
                        }

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships(Relationship.BLOCKING),
                        )
                    }
                }
            }
        }
    }

    describe("#mute") {
        context("when the server returns valid response") {
            context("and the id is mine") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .Builder()
                        .build(),
                )

                it("returns Relationships.ME") {
                    val instances = flowToList(repository.relationships(myAccount.id))

                    runBlocking {
                        repository.mute(myAccount.id)

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships.ME,
                        )
                    }
                }
            }

            context("and the id is not muting") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_NONE]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Mute(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            JSON_ACCOUNTS_RELATIONSHIP_MUTING,
                        )
                        .build(),
                )

                it("changes Relationships.NONE -> Relationships.MUTING") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        repository.mute(otherAccount.id)

                        instances should haveSize(2)
                        instances should containExactly(
                            Relationships.NONE,
                            Relationships(Relationship.MUTING),
                        )
                    }
                }
            }

            context("and the id is muting") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_MUTING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Unmute(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            JSON_ACCOUNTS_RELATIONSHIP_NONE,
                        )
                        .build(),
                )

                it("changes Relationships.BLOCKING -> Relationships.NONE") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        repository.mute(otherAccount.id)

                        instances should haveSize(2)
                        instances should containExactly(
                            Relationships(Relationship.MUTING),
                            Relationships.NONE,
                        )
                    }
                }
            }
        }
        context("when the server returns invalid response") {
            context("and the id is not muting") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_NONE]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Mute(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.mute(otherAccount.id)
                        }

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships.NONE,
                        )
                    }
                }
            }

            context("and the id is muting") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_MUTING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Unmute(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.mute(otherAccount.id)
                        }

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships(Relationship.MUTING),
                        )
                    }
                }
            }
        }
    }

    describe("#toggleReblog") {
        context("when the server returns valid response") {
            context("and the id is mine") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .Builder()
                        .build(),
                )

                it("returns Relationships.ME") {
                    val instances = flowToList(repository.relationships(myAccount.id))

                    runBlocking {
                        repository.toggleReblog(myAccount.id)

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships.ME,
                        )
                    }
                }
            }

            context("and the id is showing reblogs") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING_AND_SHOWING_REBLOGS]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Follow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING,
                        )
                        .build(),
                )

                it("changes Relationships.SHOWING_REBLOGS -> Relationships.FOLLOWING") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        repository.toggleReblog(otherAccount.id)

                        instances should haveSize(2)
                        instances should containExactly(
                            Relationships(Relationship.FOLLOWING, Relationship.SHOWING_REBLOGS),
                            Relationships(Relationship.FOLLOWING),
                        )
                    }
                }
            }

            context("and the id is hiding reblogs") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Follow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING_AND_SHOWING_REBLOGS,
                        )
                        .build(),
                )

                it("changes Relationships.FOLLOWING -> Relationships.SHOWING_REBLOGS") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        repository.toggleReblog(otherAccount.id)

                        instances should haveSize(2)
                        instances should containExactly(
                            Relationships(Relationship.FOLLOWING),
                            Relationships(Relationship.FOLLOWING, Relationship.SHOWING_REBLOGS),
                        )
                    }
                }
            }
        }
        context("when the server returns invalid response") {
            context("and the id is showing reblogs") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING_AND_SHOWING_REBLOGS]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Follow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.follow(otherAccount.id)
                        }

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships(Relationship.FOLLOWING, Relationship.SHOWING_REBLOGS),
                        )
                    }
                }
            }

            context("and the id is hiding reblogs") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Follow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.follow(otherAccount.id)
                        }

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships(Relationship.FOLLOWING),
                        )
                    }
                }
            }
        }
    }

    describe("#toggleNotify") {
        context("when the server returns valid response") {
            context("and the id is mine") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .Builder()
                        .build(),
                )

                it("returns Relationships.ME") {
                    val instances = flowToList(repository.relationships(myAccount.id))

                    runBlocking {
                        repository.toggleNotify(myAccount.id)

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships.ME,
                        )
                    }
                }
            }

            context("and the id is notifying") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING_AND_NOTIFYING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Follow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING,
                        )
                        .build(),
                )

                it("changes Relationships.NOTIFYING -> Relationships.FOLLOWING") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        repository.toggleNotify(otherAccount.id)

                        instances should haveSize(2)
                        instances should containExactly(
                            Relationships(Relationship.FOLLOWING, Relationship.NOTIFYING),
                            Relationships(Relationship.FOLLOWING),
                        )
                    }
                }
            }

            context("and the id is not notifying") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Follow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING_AND_NOTIFYING,
                        )
                        .build(),
                )

                it("changes Relationships.FOLLOWING -> Relationships.NOTIFYING") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        repository.toggleNotify(otherAccount.id)

                        instances should haveSize(2)
                        instances should containExactly(
                            Relationships(Relationship.FOLLOWING),
                            Relationships(Relationship.FOLLOWING, Relationship.NOTIFYING),
                        )
                    }
                }
            }
        }
        context("when the server returns invalid response") {
            context("and the id is notifying") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING_AND_NOTIFYING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Follow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.follow(otherAccount.id)
                        }

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships(Relationship.FOLLOWING, Relationship.NOTIFYING),
                        )
                    }
                }
            }

            context("and the id is not notifying") {
                val repository = buildRepository(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Relationships(),
                            "[$JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING]",
                        )
                        .mock(
                            Api.V1.Accounts.Id.Follow(Api.V1.Accounts.Id(id = otherAccount.id.value)),
                            HttpStatusCode.BadRequest,
                        )
                        .build(),
                )

                it("raises HttpException") {
                    val instances = flowToList(repository.relationships(otherAccount.id))

                    runBlocking {
                        shouldThrowExactly<HttpException> {
                            repository.follow(otherAccount.id)
                        }

                        instances should haveSize(1)
                        instances should containExactly(
                            Relationships(Relationship.FOLLOWING),
                        )
                    }
                }
            }
        }
    }
})

private val RelationshipMap = mapOf(
    null to JSON_ACCOUNTS_RELATIONSHIP_NONE,
    Relationship.FOLLOWING to JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING,
    Relationship.SHOWING_REBLOGS to JSON_ACCOUNTS_RELATIONSHIP_SHOWING_REBLOGS,
    Relationship.NOTIFYING to JSON_ACCOUNTS_RELATIONSHIP_NOTIFYING,
    Relationship.FOLLOWED_BY to JSON_ACCOUNTS_RELATIONSHIP_FOLLOWED_BY,
    Relationship.BLOCKING to JSON_ACCOUNTS_RELATIONSHIP_BLOCKING,
    Relationship.BLOCKED_BY to JSON_ACCOUNTS_RELATIONSHIP_BLOCKED_BY,
    Relationship.MUTING to JSON_ACCOUNTS_RELATIONSHIP_MUTING,
    Relationship.MUTING_NOTIFICATIONS to JSON_ACCOUNTS_RELATIONSHIP_MUTING_NOTIFICATIONS,
    Relationship.REQUESTED to JSON_ACCOUNTS_RELATIONSHIP_REQUESTED,
    Relationship.DOMAIN_BLOCKING to JSON_ACCOUNTS_RELATIONSHIP_DOMAIN_BLOCKING,
    Relationship.ENDORSED to JSON_ACCOUNTS_RELATIONSHIP_ENDORSED,
)

private suspend inline fun eachRelationship(
    crossinline block: suspend (String, Relationship?) -> Unit,
) = RelationshipMap.forEach { (relationship, json) ->
    block(json, relationship)
}

private suspend inline fun filterRelationship(
    vararg relationship: Relationship,
    crossinline block: suspend (String, Relationship) -> Unit
) = RelationshipMap.filterKeys { it in relationship }
    .forEach { (relationship, json) ->
        relationship ?: return@forEach

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
