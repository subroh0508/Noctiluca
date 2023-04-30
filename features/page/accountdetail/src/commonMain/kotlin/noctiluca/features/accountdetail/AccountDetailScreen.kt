package noctiluca.features.accountdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale
import noctiluca.features.accountdetail.organisms.scaffold.AccountDetailScaffold
import noctiluca.features.components.AuthorizedFeatureComposable
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.model.AccountId
import org.koin.core.component.KoinScopeComponent

internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }

@Composable
fun AccountDetailScreen(
    id: String,
    component: KoinScopeComponent,
    onBackToPreviousScreen: () -> Unit,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
) = AuthorizedFeatureComposable(component, onReload, onBackToSignIn) { scope ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
    ) {
        AccountDetailScaffold(
            AccountId(id),
            onBackToPreviousScreen = onBackToPreviousScreen,
        )
    }
}
