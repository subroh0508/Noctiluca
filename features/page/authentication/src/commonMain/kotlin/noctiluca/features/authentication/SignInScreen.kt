package noctiluca.features.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.templates.scaffold.InstanceDetailScaffold
import noctiluca.features.authentication.templates.scaffold.SearchInstanceScaffold
import noctiluca.features.authentication.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import noctiluca.features.components.FeatureComposable
import noctiluca.features.components.atoms.snackbar.LocalSnackbarHostState

internal val LocalNavigator = compositionLocalOf<SignInNavigator?> { null }
internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalAuthorizeResult = compositionLocalOf<AuthorizeResult?> { null }

@Composable
fun SignInScreen(
    domain: String?,
    authorizeResult: AuthorizeResult?,
    rootContext: ComponentContext,
    navigation: SignInNavigation,
) = SignInFeature(
    authorizeResult,
    rootContext,
    navigation,
) { page ->
    when (page) {
        is SignInNavigator.Child.MastodonInstanceList -> SearchInstanceScaffold(
            MastodonInstanceListViewModel.Provider(page),
        )

        is SignInNavigator.Child.MastodonInstanceDetail -> InstanceDetailScaffold(
            MastodonInstanceDetailViewModel.Provider(
                domain ?: page.domain,
                page,
            )
        )
    }
}

@Composable
private fun SignInFeature(
    authorizeResult: AuthorizeResult?,
    rootContext: ComponentContext,
    navigation: SignInNavigation,
    content: @Composable (SignInNavigator.Child) -> Unit,
) {
    val navigator = SignInNavigator(navigation, rootContext)

    FeatureComposable(navigator) {
        CompositionLocalProvider(
            LocalResources provides Resources(Locale.current.language),
            LocalNavigator provides navigator,
            LocalAuthorizeResult provides authorizeResult,
            LocalSnackbarHostState provides remember { SnackbarHostState() },
        ) {
            val page by navigator.childStack.subscribeAsState()

            content(page.active.instance)
        }
    }
}
