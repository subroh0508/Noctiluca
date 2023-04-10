package noctiluca.features.accountdetail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale
import noctiluca.account.model.Account
import noctiluca.features.accountdetail.state.rememberAccountDetail
import noctiluca.features.components.AuthorizedFeatureComposable
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.features.shared.account.AccountHeader
import noctiluca.model.AccountId
import org.koin.core.component.KoinScopeComponent

internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    id: String,
    component: KoinScopeComponent,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
) = AuthorizedFeatureComposable(component, onReload, onBackToSignIn) { scope ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
    ) {
        AccountDetailScaffold(AccountId(id))
    }
}

@Composable
private fun AccountDetailScaffold(
    id: AccountId,
) {
    val detail = rememberAccountDetail(id)
    val account = detail.value.attributes?.let {
        Account(it.id, it.username, it.displayName, it.url, it.avatar, it.screen)
    } ?: return


    AccountHeader(
        account,
        onClickAccountIcon = { },
    )
}
