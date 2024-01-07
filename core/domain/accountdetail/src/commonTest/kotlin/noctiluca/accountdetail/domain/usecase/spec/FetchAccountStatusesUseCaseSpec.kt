package noctiluca.accountdetail.domain.usecase.spec

import io.kotest.core.spec.style.DescribeSpec

class FetchAccountStatusesUseCaseSpec : DescribeSpec({
    /*
    val status = Status(
        StatusId("100"),
        "<p>Test Status</p>",
        warningText = null,
        "2023-03-01T00:00:00.000Z".toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
        Status.Visibility.PUBLIC,
        0,
        0,
        0,
        favourited = false,
        reblogged = false,
        bookmarked = false,
        tooter = Account(
            myAccount.id,
            myAccount.username,
            myAccount.displayName,
            myAccount.url,
            myAccount.avatar,
            myAccount.screen,
        ),
        rebloggedBy = null,
        via = Status.Via("Web", website = null),
    )

    val media = Status(
        StatusId("200"),
        "<p>Test Media</p>",
        warningText = null,
        "2023-03-01T00:00:00.000Z".toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
        Status.Visibility.PUBLIC,
        0,
        0,
        0,
        favourited = false,
        reblogged = false,
        bookmarked = false,
        tooter = Account(
            myAccount.id,
            myAccount.username,
            myAccount.displayName,
            myAccount.url,
            myAccount.avatar,
            myAccount.screen,
        ),
        rebloggedBy = null,
        via = Status.Via("Web", website = null),
    )

    describe("#execute") {
        context("when the server returns valid response") {
            context("and the query does not have max_id") {
                val testCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Id.Statuses(
                                Api.V1.Accounts.Id(
                                    id = myAccount.id.value
                                )
                            ),
                            "[$JSON_STATUS_NORMAL]"
                        )
                        .build(),
                )

                it("returns the statuses") {
                    runBlocking {
                        testCase.execute(myAccount.id, StatusesQuery.Default())
                    } should be(listOf(status))
                }
            }

            context("and the query has max_id") {
                val testCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Id.Statuses(
                                Api.V1.Accounts.Id(
                                    id = myAccount.id.value
                                )
                            ),
                            "[$JSON_STATUS_NORMAL]"
                        )
                        .build(),
                )

                it("returns the statuses") {
                    runBlocking {
                        testCase.execute(myAccount.id, StatusesQuery.Default(maxId = StatusId("101")))
                    } should be(listOf(status))
                }
            }

            context("and the query is only_media") {
                val testCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Id.Statuses(
                                Api.V1.Accounts.Id(
                                    id = myAccount.id.value
                                )
                            ),
                            "[$JSON_STATUS_MEDIA]"
                        )
                        .build(),
                )

                it("returns the statuses") {
                    runBlocking {
                        testCase.execute(myAccount.id, StatusesQuery.OnlyMedia())
                    } should be(listOf(media))
                }
            }

            context("and the query is include_replies") {
                val testCase = buildUseCase(
                    MockHttpClientEngine
                        .mock(
                            Api.V1.Accounts.Id.Statuses(
                                Api.V1.Accounts.Id(
                                    id = myAccount.id.value
                                )
                            ),
                            "[$JSON_STATUS_NORMAL]"
                        )
                        .build(),
                )

                it("returns the statuses") {
                    runBlocking {
                        testCase.execute(myAccount.id, StatusesQuery.WithReplies())
                    } should be(listOf(status))
                }
            }
        }
        context("when the server returns invalid response") {
            val testCase = buildUseCase(
                MockHttpClientEngine
                    .mock(
                        Api.V1.Accounts.Id.Statuses(
                            Api.V1.Accounts.Id(
                                id = myAccount.id.value
                            )
                        ),
                        HttpStatusCode.Unauthorized
                    )
                    .build(),
            )

            it("raises HttpUnauthorizedException") {
                shouldThrowExactly<HttpUnauthorizedException> {
                    runBlocking {
                        testCase.execute(myAccount.id, StatusesQuery.Default())
                    }
                }
            }
        }
    }
    */
})

/*
private fun buildUseCase(
    engine: MockEngine,
): FetchAccountStatusesUseCase = TestAccountDetailUseCaseComponent(
    engine,
).scope.get()
*/
