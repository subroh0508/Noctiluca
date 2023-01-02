package noctiluca.features.authentication.model

sealed class AuthorizeCode {
    internal data class Success(val code: String) : AuthorizeCode()
    internal data class Failure(val cause: Throwable) : AuthorizeCode()

    fun getCodeOrNull() = if (this is Success) code else null
    fun getErrorOrNull() = if (this is Failure) cause else null

    companion object {
        internal const val QUERY_CODE = "code"
        internal const val QUERY_ERROR = "error"
    }
}
