package noctiluca.features.accountdetail

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.model.AccountId
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform.getKoin
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
    override val key = "AccountDetail#$id"

    @Composable
    override fun Content() = AuthorizedComposable(
        LocalResources provides Resources(Locale.current.language),
    ) {
        AccountDetailScaffold(
            getScreenModel { parametersOf(AccountId(id)) },
        )
    }
}
