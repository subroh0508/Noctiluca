package noctiluca.authentication.model

import noctiluca.model.Hostname
import noctiluca.model.Uri

data class AppCredential(
    val clientId: String,
    val clientSecret: String,
    val hostname: Hostname,
    val authorizeUrl: Uri,
)
