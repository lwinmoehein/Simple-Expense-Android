package lab.justonebyte.simpleexpense.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.documentfile.provider.DocumentFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.data.CategoryRepository
import lab.justonebyte.simpleexpense.data.SettingPrefRepository
import lab.justonebyte.simpleexpense.utils.LocaleHelper
import java.io.File
import java.net.URLDecoder
import javax.inject.Inject

val DOWNLOAD_FOLDER_PERMISSION_REQUEST_CODE=1

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var categoryRepository: CategoryRepository
    @Inject
    lateinit var settingRepository: SettingPrefRepository

    val chooseDownloadFolderLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->

                val contentResolver = applicationContext.contentResolver

                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                // Check for the freshest data.
                contentResolver.takePersistableUriPermission(uri, takeFlags)

                // Handle the obtained directory URI
                Log.i("Folder:",uri.toString())
                val file = DocumentFile.fromTreeUri(applicationContext,uri)
                Log.i("Folder:can i write:",file?.canWrite().toString())
                Log.i("Folder:can i read:", file?.canRead().toString())
                val settingFile = File(applicationContext.filesDir, "chosen_download_folder.txt")
                settingFile.writeText(uri.toString())

                readablePathFromUri(uri).let { readablePath->
                    CoroutineScope(Dispatchers.Main).launch {
                        if (readablePath != null) {
                            settingRepository.updateDownloadFolder(readablePath)
                        }
                    }
                }

                if (settingFile.exists()) {
                    // Read the content from the file
                    val folderPath = settingFile.readText()

                    // Now you have the folder path, you can use it as needed
                    Log.d("FolderPath", "Chosen folder path: $folderPath")
                } else {
                    Log.d("FolderPath", "File does not exist or has not been created yet.")
                }
            }
        }
    }

    fun readablePathFromUri(uri:Uri):String?{
        val indexOfColon = uri.toString().indexOf("%3A")

        // Check if %3A is found
        if (indexOfColon != -1) {
            // Extract the substring after %3A
            val substringAfterColon = uri.toString().substring(indexOfColon + 3)


            return "/storage/emulated/0/"+URLDecoder.decode(substringAfterColon,"UTF-8")
        }

        // %3A not found in the URI
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            SuBuuApp(
                chooseDownloadFolderLauncher
            )
        }


        val settingFile = File(applicationContext.filesDir, "chosen_download_folder.txt")

        if (settingFile.exists()) {
            // Read the content from the file
            val folderPath = settingFile.readText()

            // Now you have the folder path, you can use it as needed
            Log.d("FolderPath", "Chosen folder path in main: $folderPath")
            val uri = Uri.parse(folderPath)
            Log.i("Folder:",uri.toString())

            val file = DocumentFile.fromTreeUri(applicationContext,uri)
            if (file != null) {
                Log.i("Folder:main:w:", file.canWrite().toString())
                Log.i("Folder:main:r:", file.canRead().toString())
            }

        } else {
            Log.d("FolderPath", "File does not exist or has not been created yet.")
        }


    }


    override fun attachBaseContext(base: Context) {
        LocaleHelper().setLocale(base, LocaleHelper().getLanguage(base))
        super.attachBaseContext(LocaleHelper().onAttach(base))
    }

}


