package noctiluca.features.authentication

import androidx.compose.material.Text
import androidx.compose.runtime.*
import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

@Composable
fun SignInCompose() {
    var useCase by remember { mutableStateOf<SearchMastodonInstancesUseCase?>(null) }
    var result by remember { mutableStateOf<List<String>>(listOf()) }

    DisposableEffect(null) {
        loadKoinModules(AuthenticationDomainModule())
        useCase = GlobalContext.get().get()

        onDispose { unloadKoinModules(AuthenticationDomainModule()) }
    }

    useCase ?: return

    LaunchedEffect(useCase) {
        result = useCase?.execute("imastodon.net") ?: listOf()
    }

    result.forEach { Text(it) }
}