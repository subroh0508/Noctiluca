package noctiluca.model

interface AuthorizedUser {
    val id: AccountId
    val hostname: Hostname
}
