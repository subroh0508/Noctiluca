package noctiluca.features.accountdetail.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import noctiluca.accountdetail.domain.usecase.FetchAccountAttributesUseCase
import noctiluca.features.accountdetail.AccountDetailNavigator
import noctiluca.features.accountdetail.viewmodel.statuses.AccountStatusesViewModel
import noctiluca.features.components.AuthorizedViewModel
import noctiluca.features.components.LocalCoroutineExceptionHandler
import noctiluca.features.components.UnauthorizedExceptionHandler
import noctiluca.features.components.model.LoadState
import noctiluca.model.AccountId
import org.koin.core.component.get

class AccountDetailViewModel private constructor(
    private val id: AccountId,
    private val fetchAccountAttributesUseCase: FetchAccountAttributesUseCase,
    coroutineScope: CoroutineScope,
    lifecycleRegistry: LifecycleRegistry,
    context: AccountDetailNavigator.Child.AccountDetail,
    exceptionHandler: UnauthorizedExceptionHandler,
) : AuthorizedViewModel(
    coroutineScope,
    lifecycleRegistry,
    context,
    exceptionHandler,
),
    AccountStatusesViewModel by AccountStatusesViewModel(
        id,
        coroutineScope,
        lifecycleRegistry,
        context,
        exceptionHandler,
    ) {
    private val accountDetailLoadState by lazy { MutableValue<LoadState>(LoadState.Initial) }

    fun load() {
        val job = launchLazy {
            runCatchingWithAuth { fetchAccountAttributesUseCase.execute(id) }
                .onSuccess { accountDetailLoadState.value = LoadState.Loaded(it) }
                .onFailure { accountDetailLoadState.value = LoadState.Error(it) }
        }

        accountDetailLoadState.value = LoadState.Loading(job)
        job.start()
    }

    companion object Provider {
        @Composable
        operator fun invoke(
            id: AccountId,
            context: AccountDetailNavigator.Child.AccountDetail,
        ): AccountDetailViewModel {
            val coroutineScope = rememberCoroutineScope()
            val lifecycleRegistry = remember { LifecycleRegistry() }
            val handler = LocalCoroutineExceptionHandler.current

            return remember {
                AccountDetailViewModel(
                    id,
                    context.get(),
                    coroutineScope,
                    lifecycleRegistry,
                    context,
                    handler,
                )
            }
        }
    }
}
