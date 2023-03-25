package lab.justonebyte.moneysubuu.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import lab.justonebyte.moneysubuu.api.ObjectPostData
import lab.justonebyte.moneysubuu.api.ObjectService
import lab.justonebyte.moneysubuu.data.*
import lab.justonebyte.moneysubuu.utils.RetrofitHelper

val KEY_VERSION_TABLE="version_table_name"
class GetVersionInfoWorker (
    context: Context,
    workerParams: WorkerParameters
)  : CoroutineWorker(context, workerParams) {

    private val scope =  CoroutineScope(SupervisorJob())


    override suspend fun doWork(): Result {
        val categoryDao: CategoryDao =
            AppDatabase.getDatabase(applicationContext,scope).categoryDao()
        val categoryRepository = CategoryRepositoryImpl(categoryDao)
        val transactionDao: TransactionDao =
            AppDatabase.getDatabase(applicationContext,scope).transactionDao()
        val transactionRepository = TransactionRepositoryImpl(transactionDao)

        val tableName = inputData.getString(KEY_TABLE_NAME)


        val categoryVersions = categoryRepository.getUniqueIdsWithVersions()
        val transactionVersions = transactionRepository.getUniqueIdsWithVersions()


        val objectService = RetrofitHelper.getInstance().create(ObjectService::class.java)

        if(tableName=="categories"){
            val result = objectService.getChangedCategories(ObjectPostData(versions=categoryVersions))

            result?.body()?.let { it ->
                val combinedUpdateAndNewIds = it.data.new_client_object_ids+it.data.objects_to_update_server.map {id-> id.unique_id }

                val gson = Gson()
                val newServersList = gson.toJson(it.data.new_server_objects+it.data.objects_to_update_client)

                val inputData = Data.Builder().putStringArray(KEY_NEW_CLIENTS_IDS,
                    combinedUpdateAndNewIds.toTypedArray()
                ).putString(KEY_VERSION_TABLE,tableName).putString(KEY_SERVER_OBJECTS,newServersList).build()
                return Result.success(inputData)
            }
        }else{
            val result = objectService.getChangedTransactions(ObjectPostData(versions= transactionVersions))

            result?.body()?.let { it ->
                val combinedUpdateAndNewIds = it.data.new_client_object_ids+it.data.objects_to_update_server.map {id-> id.unique_id }

                val gson = Gson()
                val newServersList = gson.toJson(it.data.new_server_objects+it.data.objects_to_update_client)

                val inputData = Data.Builder().putStringArray(KEY_NEW_CLIENTS_IDS,
                    combinedUpdateAndNewIds.toTypedArray()
                ).putString(KEY_VERSION_TABLE,tableName).putString(KEY_SERVER_OBJECTS,newServersList).build()
                return Result.success(inputData)
            }
        }



        return Result.success()
    }
}

