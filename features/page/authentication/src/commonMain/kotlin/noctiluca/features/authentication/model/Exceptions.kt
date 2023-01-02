package noctiluca.features.authentication.model

object AccessDeniedException : Exception()
object BrowserAppNotFoundException : Exception()
class AuthorizedFailedException(message: String) : Exception(message)
object UnknownException : Exception()
