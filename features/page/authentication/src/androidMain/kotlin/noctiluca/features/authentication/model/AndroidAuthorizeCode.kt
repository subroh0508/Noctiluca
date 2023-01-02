package noctiluca.features.authentication.model

import android.net.Uri

operator fun AuthorizeCode.Companion.invoke(uri: Uri?): AuthorizeCode {
    val code = uri?.getQueryParameter(QUERY_CODE)
    val error = uri?.getQueryParameter(QUERY_ERROR)

    if (code != null) {
        return AuthorizeCode.Success(code)
    }

    return when (error) {
        "access_denied" -> AuthorizeCode.Failure(AccessDeniedException)
        null -> AuthorizeCode.Failure(UnknownException)
        else -> AuthorizeCode.Failure(AuthorizedFailedException(error))
    }
}
