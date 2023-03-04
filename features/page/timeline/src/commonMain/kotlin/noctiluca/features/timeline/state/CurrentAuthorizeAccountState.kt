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
    val others: List<Account> = listOf(),
    private val loadState: LoadState = LoadState.Initial,
    private val cache: AuthorizedUser? = null,
) {
    val current get() = loadState.getValueOrNull<Account>()
    val domain get() = cache?.domain

    val loading get() = loadState.loading
}

@Composable
internal fun rememberCurrentAuthorizedAccountStatus(
    scope: Scope = LocalScope.current,
): State<CurrentAuthorizedAccount> {
    val tokenProvider: TokenProvider = remember { scope.get() }
    val fetchCurrentAuthorizedAccountUseCase: FetchCurrentAuthorizedAccountUseCase =
        remember { scope.get() }
    val fetchAllAuthorizedAccountsUseCase: FetchAllAuthorizedAccountsUseCase =
        remember { scope.get() }

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
            .onSuccess { value = value.copy(loadState = LoadState.Loaded(it)) }
            .onFailure { value = value.copy(loadState = LoadState.Error(it)) }
    }

    value = value.copy(loadState = LoadState.Loading(job))
    job.start()
}

private fun ProduceAuthorizedStateScope<CurrentAuthorizedAccount>.loadAllAccounts(
    useCase: FetchAllAuthorizedAccountsUseCase,
) {
    val job = launch(start = CoroutineStart.LAZY) {
        useCase.execute().collect {
            value = value.copy(others = value.others + it)
        }
    }

    value = value.copy(loadState = LoadState.Loading(job))
    job.start()
}
