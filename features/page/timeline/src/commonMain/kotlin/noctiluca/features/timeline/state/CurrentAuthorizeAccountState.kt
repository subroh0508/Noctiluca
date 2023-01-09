package noctiluca.features.timeline.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import noctiluca.account.model.Account
import noctiluca.features.components.model.LoadState
import noctiluca.features.components.state.ProduceAuthorizedStateScope
import noctiluca.features.components.state.produceAuthorizedState
import noctiluca.features.timeline.LocalScope
import noctiluca.model.AuthorizedUser
import noctiluca.repository.TokenProvider
import noctiluca.timeline.domain.usecase.FetchAllAuthorizedAccountsUseCase
import noctiluca.timeline.domain.usecase.FetchCurrentAuthorizedAccountUseCase
import org.koin.core.scope.Scope

internal data class CurrentAuthorizedAccount(
    private val currentLoadState: LoadState = LoadState.Initial,
    private val allLoadState: LoadState = LoadState.Initial,
    private val cache: AuthorizedUser? = null,
) {
    val current get() = currentLoadState.getValueOrNull<Account>()
    val all get() = allLoadState.getValueOrNull<List<Account>>() ?: listOf()
    val domain get() = cache?.domain ?: current?.domain

    val loading get() = currentLoadState.loading || allLoadState.loading
}

@Composable
internal fun rememberCurrentAuthorizedAccountStatus(
    scope: Scope = LocalScope.current,
): State<CurrentAuthorizedAccount> {
    val tokenProvider: TokenProvider = remember { scope.get() }
    val fetchCurrentAuthorizedAccountUseCase: FetchCurrentAuthorizedAccountUseCase = remember { scope.get() }
    val fetchAllAuthorizedAccountsUseCase: FetchAllAuthorizedAccountsUseCase = remember { scope.get() }

    return produceAuthorizedState(
        initialValue = CurrentAuthorizedAccount(),
        Unit,
    ) {
        runBlocking { value = value.copy(cache = tokenProvider.getCurrent()) }

        loadAccount(fetchCurrentAuthorizedAccountUseCase)
        loadAllAccounts(fetchAllAuthorizedAccountsUseCase)
    }
}

private fun ProduceAuthorizedStateScope<CurrentAuthorizedAccount>.loadAccount(
    useCase: FetchCurrentAuthorizedAccountUseCase,
) {
    val job = launch(start = CoroutineStart.LAZY) {
        runCatchingWithAuth { useCase.execute() }
            .onSuccess { value = value.copy(currentLoadState = LoadState.Loaded(it)) }
            .onFailure { value = value.copy(currentLoadState = LoadState.Error(it)) }
    }

    value = value.copy(currentLoadState = LoadState.Loading(job))
    job.start()
}

private fun ProduceAuthorizedStateScope<CurrentAuthorizedAccount>.loadAllAccounts(
    useCase: FetchAllAuthorizedAccountsUseCase,
) {
    val job = launch(start = CoroutineStart.LAZY) {
        runCatchingWithAuth { useCase.execute() }
            .onSuccess { value = value.copy(allLoadState = LoadState.Loaded(it)) }
            .onFailure { value = value.copy(allLoadState = LoadState.Error(it)) }
    }

    value = value.copy(allLoadState = LoadState.Loading(job))
    job.start()
}
