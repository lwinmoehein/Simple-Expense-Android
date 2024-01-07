package lab.justonebyte.simpleexpense.utils

import android.net.Uri
import java.net.URLDecoder

fun Uri?.getDecodedPath(): String? {
    // Check if the Uri is not null
    this ?: return null

    val indexOfColon = this.toString().indexOf("%3A")

    // Check if %3A is found
    if (indexOfColon != -1) {
        // Extract the substring after %3A
        val substringAfterColon = this.toString().substring(indexOfColon + 3)

        return "/storage/emulated/0/" + URLDecoder.decode(substringAfterColon, "UTF-8")
    }

    // %3A not found in the URI
    return null
}