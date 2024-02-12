package noctiluca.features.shared.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import noctiluca.data.di.AuthorizedContext
import noctiluca.features.shared.LocalAuthorizedContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.mp.KoinPlatform.getKoin

@Composable
inline fun <reified T : ScreenModel, reified R : Screen> R.getAuthorizedScreenModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val context = LocalAuthorizedContext.current
    val scope = remember { buildScreenScope<R>(context) }

    return rememberScreenModel(tag = qualifier?.value) {
        scope.get(qualifier, parameters)
    }
}

inline fun <reified R : Screen> buildScreenScope(
    context: AuthorizedContext,
): Scope {
    val screenScope = getKoin().createScope<R>()
    screenScope.linkTo(context.scope!!)

    return screenScope
}
