package noctiluca.features.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

sealed class Timeline : ScreenProvider {
    data class TimelineLane(val id: String, val domain: String) : Timeline()
    data object Toot : Timeline()
}

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
    val timelines = rememberScreen(
        Timeline.TimelineLane(id.value, domain.value),
    )

    navigator?.replaceAll(timelines)
}
