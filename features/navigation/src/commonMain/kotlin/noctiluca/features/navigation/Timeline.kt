package noctiluca.features.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

data class Timelines(val id: String, val domain: String) : ScreenProvider
data object Toot : ScreenProvider

@Composable
fun navigateToTimelines(user: AuthorizedUser?) {
    user ?: return

    navigateToTimelines(user.id, user.domain)
}

@Composable
fun navigateToTimelines(
    id: AccountId,
    domain: Domain,
) {
    val navigator = LocalNavigator.current
    val timelines = rememberScreen(Timelines(id.value, domain.value))

    navigator?.replaceAll(timelines)
}
