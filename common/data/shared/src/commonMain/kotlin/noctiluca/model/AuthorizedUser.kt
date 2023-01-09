package noctiluca.model

interface AuthorizedUser {
    val id: AccountId
    val domain: Domain
}
