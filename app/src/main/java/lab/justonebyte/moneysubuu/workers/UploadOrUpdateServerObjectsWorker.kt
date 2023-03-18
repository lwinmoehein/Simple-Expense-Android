package lab.justonebyte.moneysubuu.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.data.*
import lab.justonebyte.moneysubuu.model.ServerCategory
import lab.justonebyte.moneysubuu.model.ServerTransaction

val KEY_SERVER_OBJECTS = "new_server_objects"

class UpdateServerObjectsWorker (
    context: Context,
    workerParams: WorkerParameters
)  : CoroutineWorker(context, workerParams) {

    private val scope =  CoroutineScope(SupervisorJob())

    override suspend fun doWork(): Result {
        Log.i("work:","server objects")
        val categoryDao: CategoryDao =
            AppDatabase.getDatabase(applicationContext,scope).categoryDao()
        val transactionDao: TransactionDao =
            AppDatabase.getDatabase(applicationContext,scope).transactionDao()

            val categoryRepository = CategoryRepositoryImpl(categoryDao)
            val transactionRepository = TransactionRepositoryImpl(transactionDao)


            val newServerObjectsString = inputData.getString(KEY_SERVER_OBJECTS)
            val tableName = inputData.getString(KEY_TABLE_NAME)

            val gson = Gson()
            val listType = object : TypeToken<List<ServerCategory>>() {}.type
            val objectsList = gson.fromJson<List<ServerCategory>>(newServerObjectsString, listType)

            Log.i("objs:",objectsList.size.toString())



            if(objectsList.isNullOrEmpty()) Result.success()





             scope.launch {
                 when(tableName){
                     "transactions"-> transactionRepository.insertAll(objectsList as List<ServerTransaction>)
                     else -> categoryRepository.insertAll(objectsList)
                 }
             }


             return Result.success()
    }
}

