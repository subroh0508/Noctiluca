package noctiluca.features.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.components.FeatureComponent
import noctiluca.features.authentication.di.SignInModule

@Composable
fun SignInCompose() = FeatureComponent(SignInModule) { scope ->
    val useCase: SearchMastodonInstancesUseCase = scope.get()
    var result by remember { mutableStateOf<List<String>>(listOf()) }

    LaunchedEffect(scope) {
        result = useCase.execute(".net")
    }

    Column {
        result.forEach {
            Text(it)
        }
    }
}