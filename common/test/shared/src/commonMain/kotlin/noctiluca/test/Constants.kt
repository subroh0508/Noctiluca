package noctiluca.test

import io.ktor.http.*
import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.test.model.MockAuthorizedUser

const val DUMMY_ACCESS_TOKEN = "xxx"
const val DOMAIN_MASTODON_JP = "mstdn.jp"

val URL_MASTODON_JP = "${URLProtocol.HTTPS.name}://$DOMAIN_MASTODON_JP"

val me = MockAuthorizedUser(
    AccountId("1"),
    Domain(DOMAIN_MASTODON_JP),
)