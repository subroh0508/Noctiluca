package noctiluca.features.authentication.model

import android.os.Bundle

operator fun AuthorizeResult.Companion.invoke(arguments: Bundle): AuthorizeResult {
    val code = arguments.getString(QUERY_CODE)
    val error = arguments.getString(QUERY_ERROR)
    val errorDescription = arguments.getString(QUERY_ERROR_DESCRIPTION)

    if (code != null) {
        return AuthorizeResult.Success(code)
    }

    return when (error) {
        "access_denied" -> AuthorizeResult.Failure(AccessDeniedException(errorDescription ?: error))
        null -> AuthorizeResult.Failure(UnknownException)
        else -> AuthorizeResult.Failure(AuthorizedFailedException(errorDescription ?: error))
    }
}
