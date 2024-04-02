package noctiluca.features.shared.utils

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

private val PATTERN = "%[.,|\\d]*[sdf]|%".toRegex()
private val DIGIT = "%[.,]*(\\d*)[sdf]|%".toRegex()

// @see: https://stackoverflow.com/a/64499248
actual fun String.format(vararg args: Any?): String {
    var returnString = ""
    val singleFormats = PATTERN.findAll(this).map {
        it.groupValues.first()
    }.toList()
    val newStrings = this.split(PATTERN)
    for (i in 0 until args.count()) {
        val arg = args[i]
        returnString += when (arg) {
            is Double -> {
                stringWithFormat(newStrings[i], singleFormats[i], args[i] as Double)
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

private fun stringWithFormat(newString: String, singleFormat: String, arg: Double): String {
    if (!singleFormat.contains(",")) {
        return NSString.stringWithFormat(newString + singleFormat, arg)
    }

    val digit = DIGIT.find(singleFormat)?.groupValues?.get(1)?.toInt() ?: 0

    return stringWithFormat(newString, digit, NSNumber(arg))
}

private fun stringWithFormat(newString: String, singleFormat: String, arg: Int): String {
    if (!singleFormat.contains(",")) {
        return NSString.stringWithFormat(newString + singleFormat, arg)
    }

    return stringWithFormat(newString, 0, NSNumber(arg))
}

private fun stringWithFormat(newString: String, digit: Int, arg: NSNumber): String {
    val formattedNumber = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        minimumFractionDigits = digit.toULong()
        maximumFractionDigits = digit.toULong()
    }.stringFromNumber(arg)

    return NSString.stringWithFormat("$newString%@", formattedNumber as Any)
}
