package noctiluca.data.di

import kotlinx.coroutines.flow.Flow
import noctiluca.model.AccountId
import noctiluca.model.AuthorizeEventState
import org.koin.core.scope.Scope

interface AuthorizedContext {
    val state: Flow<AuthorizeEventState>
    val scope: Scope?

    fun reset()
    fun reopen()
    fun requestSignIn()

    suspend fun switchCurrent(id: AccountId)
    suspend fun expireCurrent()
}
