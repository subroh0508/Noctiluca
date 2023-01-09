package noctiluca.status.model

import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.Uri

data class Tooter(
    val id: AccountId,
    val username: String,
    val displayName: String,
    val url: Uri,
    val avatar: Uri,
) {
    companion object {
        private val REGEX_ACCOUNT_URL = """^https://(.*?)/@.*$""".toRegex()
    }

    val domain get() = REGEX_ACCOUNT_URL.find(url.value)?.let { Domain(it.value) }
}
