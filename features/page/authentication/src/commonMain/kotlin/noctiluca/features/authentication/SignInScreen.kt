package noctiluca.features.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.arkivanov.decompose.ComponentContext
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.templates.scaffold.HandleAuthorize
import noctiluca.features.authentication.templates.scaffold.InstanceDetailScaffold
import noctiluca.features.authentication.templates.scaffold.SearchInstanceScaffold
import noctiluca.features.authentication.viewmodel.AuthorizeViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import noctiluca.features.components.FeatureComposable
import noctiluca.features.components.atoms.snackbar.LocalSnackbarHostState

internal val LocalNavigator = compositionLocalOf<SignInNavigator.Screen?> { null }
internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalAuthorizeResult = compositionLocalOf<AuthorizeResult?> { null }

val featureSignInScreenModule = screenModule {
    register<SignInScreen.MastodonInstanceList> {
        MastodonInstanceListScreen
    }
    register<SignInScreen.MastodonInstanceDetail> { provider ->
        MastodonInstanceDetailScreen(
            provider.domain,
            provider.authorizeResult,
        )
    }
}

sealed class SignInScreen : ScreenProvider {
    object MastodonInstanceList : SignInScreen()
    data class MastodonInstanceDetail(
        val domain: String,
        val authorizeResult: AuthorizeResult? = null,
    ) : SignInScreen()

    companion object {
        @Composable
        operator fun invoke(
            handle: @Composable (Navigator) -> Unit,
        ) = Navigator(MastodonInstanceListScreen) {
            CurrentScreen()
            handle(it)
        }
    }
}

internal object MastodonInstanceListScreen : Screen {
    @Composable
    override fun Content() {
        val component = SignInComponent()

        SignInFeature(authorizeResult = null) { context ->
            SearchInstanceScaffold(
                MastodonInstanceListViewModel.Provider(
                    component,
                    context,
                ),
            )
        }
    }
}

internal data class MastodonInstanceDetailScreen(
    private val domain: String,
    private val authorizeResult: AuthorizeResult? = null,
) : Screen {
    @Composable
    override fun Content() {
        val component = SignInComponent()

        SignInFeature(authorizeResult = authorizeResult) {
            val authorizeViewModel = AuthorizeViewModel.Provider(
                domain,
                component,
            )

            HandleAuthorize(authorizeViewModel)

            InstanceDetailScaffold(
                MastodonInstanceDetailViewModel.Provider(
                    domain,
                    component,
                ),
                authorizeViewModel.isFetchingAccessToken,
            ) { authorizeViewModel.requestAuthorize(it) }
        }
    }
}

@Composable
private fun SignInFeature(
    authorizeResult: AuthorizeResult?,
    content: @Composable (ComponentContext) -> Unit,
) = CompositionLocalProvider(
    LocalResources provides Resources(Locale.current.language),
    LocalAuthorizeResult provides authorizeResult,
    LocalSnackbarHostState provides remember { SnackbarHostState() },
) { FeatureComposable(content) }
