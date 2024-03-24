package noctiluca.network.authorization.internal

import platform.Foundation.NSCharacterSet
import platform.Foundation.NSString
import platform.Foundation.URLHostAllowedCharacterSet
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters

@Suppress("CAST_NEVER_SUCCEEDS")
internal actual fun String.encode(): String =
    (this as NSString).stringByAddingPercentEncodingWithAllowedCharacters(NSCharacterSet.URLHostAllowedCharacterSet())
        ?: this
