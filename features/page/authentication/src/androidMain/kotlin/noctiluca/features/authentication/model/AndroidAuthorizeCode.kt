package noctiluca.features.authentication.model

import android.net.Uri

operator fun AuthorizeResult.Companion.invoke(uri: Uri?): AuthorizeResult {
    val code = uri?.getQueryParameter(QUERY_CODE)
    val error = uri?.getQueryParameter(QUERY_ERROR)

    if (code != null) {
        return AuthorizeResult.Success(code)
    }

    return when (error) {
        "access_denied" -> AuthorizeResult.Failure(AccessDeniedException)
        null -> AuthorizeResult.Failure(UnknownException)
        else -> AuthorizeResult.Failure(AuthorizedFailedException(error))
    }
}
