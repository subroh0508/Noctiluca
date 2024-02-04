package noctiluca.network.mastodon.paramaters.account

import kotlinx.serialization.Serializable

@Serializable
internal data class AccountFollowParameter(
    val reblogs: Boolean = true,
    val notify: Boolean = false,
    val languages: List<String>? = null,
)
