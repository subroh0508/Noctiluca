package noctiluca.features.authentication.model

import android.os.Bundle

operator fun AuthorizeResult.Companion.invoke(arguments: Bundle): AuthorizeResult? {
    val code = arguments.getString(QUERY_CODE)
    val error = arguments.getString(QUERY_ERROR)
    val errorDescription = arguments.getString(QUERY_ERROR_DESCRIPTION)

    if (code != null) {
        return AuthorizeResult.Success(code)
    }

    if (error == null || errorDescription == null) {
        return null
    }

    return when (error) {
        "access_denied" -> AuthorizeResult.Failure(AccessDeniedException(errorDescription))
        else -> AuthorizeResult.Failure(AuthorizedFailedException(errorDescription))
    }
}
