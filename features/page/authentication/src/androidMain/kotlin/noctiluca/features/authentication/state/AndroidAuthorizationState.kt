package noctiluca.features.authentication.state

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import noctiluca.components.model.LoadState
import noctiluca.model.AuthorizedUser

@Composable
internal actual fun transitTimeline(authorizedUserState: LoadState) {
    if (authorizedUserState.getValueOrNull<AuthorizedUser>() == null) {
        return
    }

    val navController = rememberNavController()

    // navController.navigate()
}
