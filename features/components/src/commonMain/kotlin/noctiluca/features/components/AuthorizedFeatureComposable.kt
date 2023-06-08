package noctiluca.features.components

import androidx.compose.runtime.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.repository.TokenProvider
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope

internal val LocalCoroutineExceptionHandler = compositionLocalOf { UnauthorizedExceptionHandler() }

@Composable
fun AuthorizedFeatureComposable(
    component: KoinScopeComponent,
    navigation: Navigation,
    content: @Composable (Scope) -> Unit,
) = FeatureComposable(component) {
    CompositionLocalProvider(
        LocalCoroutineExceptionHandler provides UnauthorizedExceptionHandler(
            it.get(),
            navigation,
        ),
    ) { content(it) }
}

class UnauthorizedExceptionHandler(
    private val tokenProvider: TokenProvider? = null,
    private val navigation: Navigation? = null,
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
            navigation?.reopenApp()
            return
        }

        navigation?.backToSignIn()
    }
}
