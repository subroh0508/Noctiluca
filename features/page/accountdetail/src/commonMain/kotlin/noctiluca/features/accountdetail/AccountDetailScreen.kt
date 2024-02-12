package noctiluca.features.accountdetail

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import noctiluca.features.accountdetail.screen.AccountDetailContent
import noctiluca.features.accountdetail.screen.AccountDetailScaffold
import noctiluca.features.accountdetail.section.AccountDetailTopAppBar
import noctiluca.features.accountdetail.viewmodel.AccountRelationshipsViewModel
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
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
    val id: String,
) : Screen {
    override val key = "AccountDetail#$id"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() = AuthorizedComposable(
        LocalResources provides Resources(Locale.current.language),
    ) { context ->
        val viewModel: AccountRelationshipsViewModel = getAuthorizedScreenModel(context) {
            parametersOf(AccountId(id))
        }
        val relationshipsModel by viewModel.uiModel.collectAsState()

        AccountDetailScaffold(
            context,
            relationshipsModel,
            topBar = { account, scrollBehavior ->
                AccountDetailTopAppBar(
                    account,
                    relationshipsModel,
                    scrollBehavior,
                    mute = { viewModel.mute() },
                    block = { viewModel.block() },
                    report = {},
                    toggleReblogs = { viewModel.toggleReblogs() },
                )
            },
        ) { attributesModel, paddingValues, scrollBehavior ->
            AccountDetailContent(
                context,
                paddingValues,
                attributesModel,
                relationshipsModel,
                scrollBehavior,
                follow = { viewModel.follow() },
                block = { viewModel.block() },
                notifyNewStatus = { viewModel.toggleNotify() },
            )
        }
    }
}
