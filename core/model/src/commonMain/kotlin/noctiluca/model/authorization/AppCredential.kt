package noctiluca.model.authorization

import noctiluca.model.Domain
import noctiluca.model.Uri

data class AppCredential(
    val clientId: String,
    val clientSecret: String,
    val domain: Domain,
    val authorizeUrl: Uri,
)
