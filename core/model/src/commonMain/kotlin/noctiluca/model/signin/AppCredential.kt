package noctiluca.model.signin

import noctiluca.model.Domain
import noctiluca.model.Uri

data class AppCredential(
    val clientId: String,
    val clientSecret: String,
    val domain: Domain,
    val authorizeUrl: Uri,
)
