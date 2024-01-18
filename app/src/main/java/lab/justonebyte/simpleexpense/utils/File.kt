package lab.justonebyte.simpleexpense.utils

import android.content.Context
import java.io.File
import java.io.IOException

val defaultFileName = "is_onboarding_done.txt"
fun createIsOnboardDoneFlagFile(context: Context, fileName: String= defaultFileName): File {
    val file = File(context.filesDir, fileName)
    try {
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    } catch (e: IOException) {
        throw RuntimeException("Error creating file $fileName", e)
    }
}

fun isOnboardingDoneFlagExist(context: Context, fileName: String = defaultFileName): Boolean {
    val file = File(context.filesDir, fileName)
    return file.exists()
}