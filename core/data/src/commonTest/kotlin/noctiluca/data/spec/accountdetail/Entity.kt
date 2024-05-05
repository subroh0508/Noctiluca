package noctiluca.data.spec.accountdetail

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.model.account.Account
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.status.Attachment
import noctiluca.model.status.Status
import noctiluca.test.shared.ACCOUNT_ID
import noctiluca.test.shared.URL_SAMPLE_COM

val myAccount = AccountAttributes(
    AccountId(ACCOUNT_ID),
    "test1",
    "サンプル太郎",
    Uri("$URL_SAMPLE_COM/@test1"),
    Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
    Uri("$URL_SAMPLE_COM/accounts/headers/header.png"),
    "@test1",
    "<p>note</p>",
    100,
    100,
    1000,
    locked = false,
    bot = false,
    null,
    listOf(
        AccountAttributes.Field("フィールド1", "ほげほげ"),
    ),
    "2019-04-01T00:00:00.000Z".toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
    null,
)

val otherAccount = AccountAttributes(
    AccountId("10"),
    "test2",
    "サンプル次郎",
    Uri("$URL_SAMPLE_COM/@test2"),
    Uri("$URL_SAMPLE_COM/accounts/avatars/avater.png"),
    Uri("$URL_SAMPLE_COM/accounts/headers/header.png"),
    "@test2",
    "<p>note</p>",
    100,
    100,
    1000,
    locked = false,
    bot = false,
    null,
    listOf(
        AccountAttributes.Field("フィールド1", "ふがふが"),
    ),
    "2019-04-01T00:00:00.000Z".toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
    null,
)

val status = Status(
    StatusId("100"),
    "<p>Test Status</p>",
    warningText = null,
    "2023-03-01T00:00:00.000Z".toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
    Status.Visibility.PUBLIC,
    sensitive = false,
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
    attachments = listOf(),
)

val prevStatus = Status(
    StatusId("99"),
    "<p>Test Status</p>",
    warningText = null,
    "2023-02-28T00:00:00.000Z".toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
    Status.Visibility.PUBLIC,
    sensitive = false,
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
    attachments = listOf(),
)

val media = Status(
    StatusId("200"),
    "<p>Test Media</p>",
    warningText = null,
    Instant.parse("2023-03-01T00:00:00.000Z").toLocalDateTime(TimeZone.of("Asia/Tokyo")),
    Status.Visibility.PUBLIC,
    sensitive = false,
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
    attachments = listOf(
        Attachment.Image(
            Uri("$URL_SAMPLE_COM/media_attachments/files/sample_200.jpeg"),
            Uri("$URL_SAMPLE_COM/media_attachments/files/small_200.jpeg"),
            description = null,
        ),
    ),
)

val prevMedia = Status(
    StatusId("199"),
    "<p>Test Media</p>",
    warningText = null,
    Instant.parse("2023-02-28T00:00:00.000Z").toLocalDateTime(TimeZone.of("Asia/Tokyo")),
    Status.Visibility.PUBLIC,
    sensitive = false,
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
    attachments = listOf(
        Attachment.Image(
            Uri("$URL_SAMPLE_COM/media_attachments/files/sample_199.jpeg"),
            Uri("$URL_SAMPLE_COM/media_attachments/files/small_199.jpeg"),
            description = null,
        ),
    ),
)
