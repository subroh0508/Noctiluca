package noctiluca.features.signin.model

import noctiluca.features.navigation.MastodonInstanceDetailParams

private val QUERY_CODE_PATTERN = """${AuthorizeResult.QUERY_CODE}=([^&]*)""".toRegex()
private val QUERY_ERROR_PATTERN = """${AuthorizeResult.QUERY_ERROR}=([^&]*)""".toRegex()
private val QUERY_ERROR_DESCRIPTION_PATTERN =
    """${AuthorizeResult.QUERY_ERROR_DESCRIPTION}=([^&]*)""".toRegex()

internal fun buildAuthorizeResult(params: MastodonInstanceDetailParams): AuthorizeResult? {
    val code = QUERY_CODE_PATTERN.find(params.query ?: "")?.groupValues?.get(1)
    val error = QUERY_ERROR_PATTERN.find(params.query ?: "")?.groupValues?.get(1)
    val errorDescription =
        QUERY_ERROR_DESCRIPTION_PATTERN.find(params.query ?: "")?.groupValues?.get(1)

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
