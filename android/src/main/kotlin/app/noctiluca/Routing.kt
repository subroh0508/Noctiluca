package app.noctiluca

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import app.noctiluca.navigation.*
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import noctiluca.features.accountdetail.AccountDetailNavigator
import noctiluca.features.accountdetail.AccountDetailScreen

@Composable
fun Routing(navigator: AndroidNavigator) {
    val feature by navigator.childStack.subscribeAsState()

    feature.active.instance.let {
        when (it) {
            is AndroidNavigator.Feature.SignIn -> Unit

            is AndroidNavigator.Feature.Timeline -> Unit

            is AndroidNavigator.Feature.AccountDetail -> AccountDetailScreen(
                AccountDetailNavigator.Screen(it.id, navigator),
            )
        }
    }
}
