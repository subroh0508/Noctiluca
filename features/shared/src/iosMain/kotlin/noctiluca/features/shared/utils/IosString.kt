package noctiluca.features.shared.utils

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

// @see: https://stackoverflow.com/a/64499248
actual fun String.format(vararg args: Any?): String {
    var returnString = ""
    val regEx = "%[\\d|.]*[sdf]|%".toRegex()
    val singleFormats = regEx.findAll(this).map {
        it.groupValues.first()
    }.toList()
    val newStrings = this.split(regEx)
    for (i in 0 until args.count()) {
        val arg = args[i]
        returnString += when (arg) {
            is Double -> {
                NSString.stringWithFormat(newStrings[i] + singleFormats[i], args[i] as Double)
            }

            is Int -> {
                NSString.stringWithFormat(newStrings[i] + singleFormats[i], args[i] as Int)
            }

            else -> {
                NSString.stringWithFormat(newStrings[i] + "%@", args[i])
            }
        }
    }

    if (newStrings.count() > args.count()) {
        returnString += newStrings.last()
    }

    return returnString
}
