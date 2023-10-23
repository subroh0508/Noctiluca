package noctiluca.features.components

import androidx.compose.runtime.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import noctiluca.datastore.TokenDataStore
import noctiluca.model.AuthorizedTokenNotFoundException
import org.koin.core.component.get

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
    private val dataStore: TokenDataStore? = null,
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
            expireCurrent()
            getCurrent()
        }

        if (nextAuthorizedUser != null) {
            navigator?.reopenApp()
            return
        }

        navigator?.backToSignIn()
    }

    private suspend fun expireCurrent() {
        dataStore?.getCurrent()?.let { dataStore.delete(it.id) }
        dataStore?.getAll()?.firstOrNull()?.let {
            dataStore.setCurrent(it.id)
        }
    }

    private suspend fun getCurrent() = dataStore?.getCurrent()
}
