package noctiluca.features.timeline.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import noctiluca.account.model.Account
import noctiluca.features.components.Navigation
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.features.components.state.*
import noctiluca.features.timeline.LocalScope
import noctiluca.model.Domain
import noctiluca.repository.TokenProvider
import noctiluca.timeline.domain.usecase.FetchAllAuthorizedAccountsUseCase
import noctiluca.timeline.domain.usecase.FetchCurrentAuthorizedAccountUseCase
import org.koin.core.scope.Scope

internal data class CurrentAuthorizedAccount(
    val current: Account? = null,
    val domain: Domain? = null,
    val others: List<Account> = listOf(),
)

internal class CurrentAuthorizedAccountState(
    private val accountState: MutableState<CurrentAuthorizedAccount>,
    private val state: AuthorizedComposeState,
    private val tokenProvider: TokenProvider,
    private val fetchCurrentAuthorizedAccountUseCase: FetchCurrentAuthorizedAccountUseCase,
    private val fetchAllAuthorizedAccountsUseCase: FetchAllAuthorizedAccountsUseCase,
    private val reload: () -> Unit,
) : State<CurrentAuthorizedAccount> by accountState, AuthorizedComposeState by state {
    constructor(
        state: AuthorizedComposeState = AuthorizedComposeState(),
        koinScope: Scope = getKoinRootScope(),
        reload: () -> Unit = {},
    ) : this(
        mutableStateOf(CurrentAuthorizedAccount()),
        state,
        koinScope.get(),
        koinScope.get(),
        koinScope.get(),
        reload,
    )

    fun switch(scope: CoroutineScope, account: Account) {
        scope.launch {
            tokenProvider.switch(account.id)
            reload()
        }
    }

    fun loadCurrentAccount(scope: CoroutineScope) {
        scope.launch {
            runCatchingWithAuth {
                fetchCurrentAuthorizedAccountUseCase.execute().collect { (account, domain) ->
                    setCurrent(account, domain)
                }
            }
        }
    }

    fun loadOtherAccounts(scope: CoroutineScope) {
        scope.launch {
            fetchAllAuthorizedAccountsUseCase.execute().collect {
                setOthers(it)
            }
        }
    }

    private fun setCurrent(account: Account, domain: Domain) {
        accountState.value = accountState.value.copy(current = account, domain = domain)
    }

    private fun setOthers(account: Account) {
        val prevOthers = accountState.value.others
        val nextOthers = prevOthers.filterNot { it.id == account.id } + account

        accountState.value = accountState.value.copy(others = nextOthers)
    }
}

@Composable
internal fun rememberCurrentAuthorizedAccountStatus(
    navigation: Navigation,
    scope: Scope = LocalScope.current,
): CurrentAuthorizedAccountState {
    val authorizationState = rememberAuthorizedComposeState()
    val currentAuthorizedAccount = remember {
        CurrentAuthorizedAccountState(
            authorizationState,
            scope,
        ) { navigation.reopenApp() }
    }

    LaunchedEffect(Unit) {
        currentAuthorizedAccount.loadCurrentAccount(this)
        currentAuthorizedAccount.loadOtherAccounts(this)
    }

    return currentAuthorizedAccount
}
