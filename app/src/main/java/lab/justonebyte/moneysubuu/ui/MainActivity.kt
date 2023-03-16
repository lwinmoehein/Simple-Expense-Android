package lab.justonebyte.moneysubuu.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.*

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.data.CategoryRepository
import lab.justonebyte.moneysubuu.model.ServerCategory
import lab.justonebyte.moneysubuu.utils.LocaleHelper
import lab.justonebyte.moneysubuu.workers.GetVersionInfoWorker
import lab.justonebyte.moneysubuu.workers.KEY_TABLE_NAME
import lab.justonebyte.moneysubuu.workers.UploadOrUpdateClientObjectsWorker
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var categoryRepository: CategoryRepository

    var allCategories:List<ServerCategory> = listOf()
    private val workManager = WorkManager.getInstance(application)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            SuBuuApp()
        }

        lifecycleScope.launch {
            runVersionSync("categories")
        }
        lifecycleScope.launch{
            runVersionSync("transactions")
        }
    }
    fun runVersionSync(tableName:String){
        var continuation = workManager
            .beginWith(
                OneTimeWorkRequest.Builder(GetVersionInfoWorker::class.java)
                    .setInputData(Data.Builder().putString(KEY_TABLE_NAME,tableName).build())
                    .build()
            )

        // Add WorkRequest to blur the image
        val blurRequest = OneTimeWorkRequest.Builder(UploadOrUpdateClientObjectsWorker::class.java)
            .build()

        continuation = continuation.then(blurRequest)

        // Actually start the work
        continuation.enqueue()
    }

    override fun attachBaseContext(base: Context) {
        LocaleHelper().setLocale(base, LocaleHelper().getLanguage(base))
        super.attachBaseContext(LocaleHelper().onAttach(base))
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

