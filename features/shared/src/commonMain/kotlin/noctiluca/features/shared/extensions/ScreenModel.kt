package noctiluca.features.shared.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatform.getKoin

@Composable
inline fun <reified T : ScreenModel, reified R : Screen> R.getFeaturesScreenModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val scope = remember { getKoin().createScope<R>() }
    return rememberScreenModel(tag = qualifier?.value) {
        scope.get(qualifier, parameters)
    }
}
