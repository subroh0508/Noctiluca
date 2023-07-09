package noctiluca.features.authentication.model

import noctiluca.features.authentication.model.AuthorizeResult.Companion.QUERY_CODE
import noctiluca.features.authentication.model.AuthorizeResult.Companion.QUERY_ERROR
import noctiluca.features.authentication.model.AuthorizeResult.Companion.QUERY_ERROR_DESCRIPTION

private val QUERY_CODE_PATTERN = """$QUERY_CODE=([^&]*)""".toRegex()
private val QUERY_ERROR_PATTERN = """$QUERY_ERROR=([^&]*)""".toRegex()
private val QUERY_ERROR_DESCRIPTION_PATTERN = """$QUERY_ERROR_DESCRIPTION=([^&]*)""".toRegex()

operator fun AuthorizeResult.Companion.invoke(query: String?): AuthorizeResult? {
    val code = QUERY_CODE_PATTERN.find(query ?: "")?.groupValues?.get(1)
    val error = QUERY_ERROR_PATTERN.find(query ?: "")?.groupValues?.get(1)
    val errorDescription = QUERY_ERROR_DESCRIPTION_PATTERN.find(query ?: "")?.groupValues?.get(1)

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
