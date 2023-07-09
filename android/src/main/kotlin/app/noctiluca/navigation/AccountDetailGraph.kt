package app.noctiluca.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import noctiluca.features.accountdetail.AccountDetailScreen
import noctiluca.features.components.Navigation

const val RouteAccountDetail = "AccountDetail"

fun NavGraphBuilder.accountDetail(
    navigation: Navigation,
) = composable("$RouteAccountDetail/{id}") { navBackStackEntry ->
    val id = navBackStackEntry.arguments?.getString("id") ?: return@composable

    AccountDetailScreen(
        id,
        navigation,
    )
}
