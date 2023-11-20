package noctiluca.features.accountdetail

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import noctiluca.features.accountdetail.templates.scaffold.AccountDetailScaffold
import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.model.AccountId
import org.koin.core.parameter.parametersOf
import noctiluca.features.navigation.AccountDetail as NavigationAccountDetailScreen

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureAccountDetailScreenModule = screenModule {
    register<NavigationAccountDetailScreen> { provider ->
        AccountDetailScreen(provider.id)
    }
}

internal data class AccountDetailScreen(
    private val id: String,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel: AccountDetailViewModel = getScreenModel { parametersOf(AccountId(id)) }

        AuthorizedComposable(
            viewModel,
            LocalResources provides Resources(Locale.current.language),
        ) { AccountDetailScaffold(viewModel) }
    }
}
