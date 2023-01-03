package noctiluca.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.runBlocking
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
        if (exception !is ResponseException) {
            return
        }

        if (exception.response.status != HttpStatusCode.Unauthorized) {
            return
        }

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
