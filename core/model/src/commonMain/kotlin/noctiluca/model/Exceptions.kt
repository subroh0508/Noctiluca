package noctiluca.model

data object AuthorizedTokenNotFoundException : Exception()
data object AuthorizedAccountNotFoundException : Exception()

open class HttpException(val code: Int, cause: Throwable) : Exception(cause)
class HttpUnauthorizedException(code: Int, cause: Throwable) : HttpException(code, cause)
