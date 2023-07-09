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
internal val LocalScope = compositionLocalOf { getKoinRootScope() }

@Composable
fun AccountDetailScreen(
    id: String,
    navigation: Navigation,
) = AccountDetailFeature(
    id,
    navigation,
) { page ->
    when (page) {
        is AccountDetailNavigator.Child.AccountDetail -> AccountDetailScaffold(
            AccountDetailViewModel.Provider(page),
        )
    }
}

@Composable
private fun AccountDetailFeature(
    id: String,
    navigation: Navigation,
    content: @Composable (AccountDetailNavigator.Child) -> Unit,
) = AuthorizedFeatureComposable(
    context = AccountDetailNavigator(id),
    navigation,
) { navigator ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalNavigator provides navigator,
    ) {
        val page by navigator.childStack.subscribeAsState()

        content(page.active.instance)
    }
}
