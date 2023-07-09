package noctiluca.features.components

import androidx.compose.runtime.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.repository.TokenProvider
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.get
import org.koin.core.scope.Scope

val LocalCoroutineExceptionHandler = compositionLocalOf { UnauthorizedExceptionHandler() }

@Composable
fun <T : Navigator.Screen> AuthorizedFeatureComposable(
    context: T,
    navigator: Navigator,
    content: @Composable (T) -> Unit,
) = FeatureComposable(context = context) {
    CompositionLocalProvider(
        LocalCoroutineExceptionHandler provides UnauthorizedExceptionHandler(
            it.get(),
            navigator,
        ),
    ) { content(it) }
}

class UnauthorizedExceptionHandler(
    private val tokenProvider: TokenProvider? = null,
    private val navigator: Navigator? = null,
) {
    fun handleException(exception: Throwable) {
        exception.printStackTrace()

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
            navigator?.reopenApp()
            return
        }

        navigator?.backToSignIn()
    }
}
