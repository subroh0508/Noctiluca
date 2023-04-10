package app.noctiluca.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import noctiluca.features.accountdetail.AccountDetailScreen
import noctiluca.features.accountdetail.di.AccountDetailComponent

const val RouteAccountDetail = "AccountDetail"

fun NavGraphBuilder.accountDetail(
    onBack: () -> Unit,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
) {
    composable("$RouteAccountDetail/{id}") { navBackStackEntry ->
        val id = navBackStackEntry.arguments?.getString("id") ?: return@composable

        AccountDetailScreen(id, AccountDetailComponent(), onReload, onBackToSignIn)
    }
}

fun NavController.navigateToAccountDetail(id: String) {
    navigate("$RouteAccountDetail/$id")
}
