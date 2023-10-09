package noctiluca.api.authentication.internal

import java.net.URLEncoder

internal actual fun String.encode(): String = URLEncoder.encode(this, "UTF-8")
