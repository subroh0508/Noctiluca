package noctiluca.features.authentication.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.model.AuthorizedUser
import org.koin.core.scope.Scope

@Composable
fun rememberAuthorizedUser(
    code: String?,
    scope: Scope = LocalScope.current,
): State<AuthorizedUser?> {
    val useCase: RequestAccessTokenUseCase = remember { scope.get() }
    val redirectUri = buildRedirectUri()

    return produceState<AuthorizedUser?>(
        initialValue = null,
        code,
    ) {
        if (code.isNullOrBlank()) {
            value = null
            return@produceState
        }

        runCatching { useCase.execute(code, redirectUri) }
            .onSuccess { value = it }
            .onFailure {
                it.printStackTrace()
                value = null
            }
    }
}
