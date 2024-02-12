package noctiluca.features.statusdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.features.statusdetail.templates.StatusDetailScaffold
import noctiluca.model.StatusId
import org.koin.core.parameter.parametersOf
import noctiluca.features.navigation.StatusDetail as NavigationStatusDetailScreen

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureStatusDetailScreenModule = screenModule {
    register<NavigationStatusDetailScreen> { provider ->
        StatusDetailScreen(provider.id)
    }
}

internal data class StatusDetailScreen(
    private val id: String,
) : Screen {
    override val key: ScreenKey = "StatusDetail#$id"

    @Composable
    override fun Content() = AuthorizedComposable(
        LocalResources provides Resources(Locale.current.language),
    ) { StatusDetailScaffold(getAuthorizedScreenModel { parametersOf(StatusId(id)) }) }
}
