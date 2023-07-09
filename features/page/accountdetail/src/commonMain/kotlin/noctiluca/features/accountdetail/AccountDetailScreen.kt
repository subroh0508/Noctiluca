package noctiluca.features.accountdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.intl.Locale
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import noctiluca.features.accountdetail.templates.scaffold.AccountDetailScaffold
import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import noctiluca.features.components.AuthorizedFeatureComposable
import noctiluca.features.components.Navigation
import noctiluca.features.components.di.getKoinRootScope

internal val LocalNavigator = compositionLocalOf<AccountDetailNavigator?> { null }
internal val LocalResources = compositionLocalOf { Resources("JA") }

@Composable
fun AccountDetailScreen(
    screen: AccountDetailNavigator.Screen,
) = AccountDetailFeature(screen) {
    AccountDetailScaffold(AccountDetailViewModel.Provider(screen))
}

@Composable
private fun AccountDetailFeature(
    screen: AccountDetailNavigator.Screen,
    content: @Composable () -> Unit,
) = AuthorizedFeatureComposable(
    context = screen,
    navigator = screen,
) { navigator ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalNavigator provides navigator,
    ) { content() }
}
