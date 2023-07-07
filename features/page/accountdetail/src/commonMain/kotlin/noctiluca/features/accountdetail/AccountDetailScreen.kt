package noctiluca.features.accountdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale
import com.arkivanov.decompose.ComponentContext
import noctiluca.features.accountdetail.templates.scaffold.AccountDetailScaffold
import noctiluca.features.components.AuthorizedFeatureComposable
import noctiluca.features.components.Navigation
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.model.AccountId

internal val LocalNavigator = compositionLocalOf<AccountDetailNavigator?> { null }
internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }

@Composable
fun AccountDetailScreen(
    id: String,
    rootContext: ComponentContext,
    navigation: Navigation,
) = AuthorizedFeatureComposable(
    context = AccountDetailNavigator(rootContext),
    navigation
) { navigator ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalNavigator provides navigator,
    ) { AccountDetailScaffold(AccountId(id)) }
}
