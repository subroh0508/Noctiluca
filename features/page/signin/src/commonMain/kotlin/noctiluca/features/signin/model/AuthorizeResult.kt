package noctiluca.features.signin.model

sealed class AuthorizeResult {
    internal data class Success(val code: String) : AuthorizeResult()
    internal data class Failure(val cause: Throwable) : AuthorizeResult()

    fun getCodeOrNull() = if (this is Success) code else null
    fun getErrorOrNull() = if (this is Failure) cause else null

    companion object {
        const val QUERY_DOMAIN = "domain"
        internal const val QUERY_CODE = "code"
        internal const val QUERY_ERROR = "error"
        internal const val QUERY_ERROR_DESCRIPTION = "error_description"

        val Query = listOf(
            "$QUERY_DOMAIN={$QUERY_DOMAIN}",
            "$QUERY_CODE={$QUERY_CODE}",
            "$QUERY_ERROR={$QUERY_ERROR}",
            "$QUERY_ERROR_DESCRIPTION={$QUERY_ERROR_DESCRIPTION}"
        ).joinToString("&")
    }
}
