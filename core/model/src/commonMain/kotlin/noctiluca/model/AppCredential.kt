package noctiluca.model

data class AppCredential(
    val clientId: String,
    val clientSecret: String,
    val domain: Domain,
    val authorizeUrl: Uri,
)
