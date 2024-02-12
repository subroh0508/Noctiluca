package noctiluca.features.signin.model

class AccessDeniedException(message: String) : Exception(message)
object BrowserAppNotFoundException : Exception()
class AuthorizedFailedException(message: String) : Exception(message)
object UnknownException : Exception()
