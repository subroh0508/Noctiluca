package noctiluca.components

import androidx.compose.runtime.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.runBlocking
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.repository.TokenProvider
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal val LocalCoroutineExceptionHandler = compositionLocalOf<CoroutineExceptionHandler> { UnauthorizedExceptionHandler() }

@Composable
fun AuthorizedFeatureComposable(
    component: KoinScopeComponent,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
    content: @Composable (Scope) -> Unit,
) = FeatureComposable(component) {
    CompositionLocalProvider(
        LocalCoroutineExceptionHandler provides UnauthorizedExceptionHandler(
            it.get(),
            onReload,
            onBackToSignIn,
        ),
    ) { content(it) }
}

private class UnauthorizedExceptionHandler(
    private val tokenProvider: TokenProvider? = null,
    private val navigateToTimeline: () -> Unit = {},
    private val navigateToSignIn: () -> Unit = {},
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        when (exception) {
            is AuthorizedTokenNotFoundException -> tryOtherToken()
            is ResponseException -> tryOtherToken(exception.response.status)
        }
    }

    private fun tryOtherToken(code: HttpStatusCode) {
        if (code != HttpStatusCode.Unauthorized) {
            return
        }

        tryOtherToken()
    }

    private fun tryOtherToken() {
        val nextAuthorizedUser = runBlocking {
            tokenProvider?.expireCurrent()
            tokenProvider?.getCurrent()
        }

        if (nextAuthorizedUser != null) {
            navigateToTimeline()
            return
        }

        navigateToSignIn()
    }
}
