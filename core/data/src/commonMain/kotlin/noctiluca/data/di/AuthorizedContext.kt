package noctiluca.data.di

import kotlinx.coroutines.flow.StateFlow
import noctiluca.model.AuthorizeEventState
import org.koin.core.scope.Scope

interface AuthorizedContext {
    val state: StateFlow<AuthorizeEventState>
    val scope: Scope?

    fun reset()
    fun reopen()
    fun requestSignIn()

    suspend fun expireCurrent()
}
