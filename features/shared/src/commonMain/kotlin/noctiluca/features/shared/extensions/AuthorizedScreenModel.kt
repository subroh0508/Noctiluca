package noctiluca.features.shared.extensions

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import noctiluca.data.di.AuthorizedContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

@Composable
inline fun <reified T : ScreenModel, reified R : Screen> R.getAuthorizedScreenModel(
    context: AuthorizedContext,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    return rememberScreenModel(tag = qualifier?.value) {
        context.scope!!.get(qualifier, parameters)
    }
}
