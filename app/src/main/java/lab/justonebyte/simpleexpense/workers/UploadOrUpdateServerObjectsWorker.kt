package lab.justonebyte.simpleexpense.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.data.*
import lab.justonebyte.simpleexpense.model.ServerCategory
import lab.justonebyte.simpleexpense.model.ServerTransaction

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
            val tableName = inputData.getString(KEY_VERSION_TABLE)

        if (tableName != null) {
            Log.i("update: mobile:",tableName)
        }else{
            Log.i("update: mobile:","null")
        }

            if(tableName=="transactions"){
                val gson = Gson()
                val listType = object : TypeToken<List<ServerTransaction>>() {}.type
                val objectsList = gson.fromJson<List<ServerTransaction>>(newServerObjectsString, listType)


                if(objectsList.isNullOrEmpty()) return Result.success()


                scope.launch {
                   transactionRepository.insertAll(objectsList)
                }
                return Result.success(inputData)
            }else{
                val gson = Gson()
                val listType = object : TypeToken<List<ServerCategory>>() {}.type
                val objectsList = gson.fromJson<List<ServerCategory>>(newServerObjectsString, listType)


                if(objectsList.isNullOrEmpty()) return Result.success()


                scope.launch {
                    categoryRepository.insertAll(objectsList)
                }
                return Result.success(inputData)
            }

    }
}

