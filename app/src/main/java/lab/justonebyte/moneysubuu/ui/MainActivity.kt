package lab.justonebyte.moneysubuu.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import lab.justonebyte.moneysubuu.utils.LocaleHelper
import lab.justonebyte.moneysubuu.workers.UpdateDBWorker
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var categoryRepository: CategoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            SuBuuApp()
        }

                  lifecycleScope.launch {
                      val uniqueIdsWithVersions = categoryRepository.getUniqueIdsWithVersions()
                      if(uniqueIdsWithVersions.size>0) {
                          val pairs = uniqueIdsWithVersions.map { it.uniqueId to (it.version ?: 1) }
                          val inputData = Data.Builder().apply {
                              putStringArray("keys", pairs.map { it.first }.toTypedArray())
                              putIntArray("values", pairs.map { it.second }.toIntArray())
                          }.build()



                          val workRequest = OneTimeWorkRequestBuilder<UpdateDBWorker>()
                              .setInputData(inputData)
                              .build()

                          WorkManager.getInstance(applicationContext)
                              .enqueue(workRequest)
                      }
                  }



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

