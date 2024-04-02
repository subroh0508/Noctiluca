package noctiluca.features.shared.utils

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

// @see: https://stackoverflow.com/a/64499248
actual fun String.format(vararg args: Any?): String {
    var returnString = ""
    val regEx = "%[\\d|.,]*[sdf]|%".toRegex()
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
                stringWithFormat(newStrings[i], singleFormats[i], args[i] as Int)
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

private fun stringWithFormat(newString: String, singleFormat: String, arg: Int): String {
    if (!singleFormat.contains(",")) {
        return NSString.stringWithFormat(newString + singleFormat, arg)
    }

    val formattedNumber = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
    }.stringFromNumber(NSNumber(arg))

    return NSString.stringWithFormat("$newString%@", formattedNumber as Any)
}
