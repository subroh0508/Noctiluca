package noctiluca.model.extensions

import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL

fun NSURL.readData(): NSData {
    var result: NSData? = null
    while (result == null) {
        val data = NSData.dataWithContentsOfURL(this)
        if (data != null) {
            result = data
        }
    }

    return result
}
