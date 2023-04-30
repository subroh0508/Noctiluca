package noctiluca.accountdetail.domain

import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.accountdetail.model.Relationships
import noctiluca.model.AccountId
import noctiluca.model.Uri
import noctiluca.test.ACCOUNT_ID
import noctiluca.test.URL_SAMPLE_COM

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
    Relationships.ME,
    null,
    listOf(
        AccountAttributes.Field("フィールド1", "ほげほげ"),
    ),
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
    Relationships.NONE,
    null,
    listOf(
        AccountAttributes.Field("フィールド1", "ふがふが"),
    ),
    null,
)
