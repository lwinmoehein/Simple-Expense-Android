package lab.justonebyte.simpleexpense.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import lab.justonebyte.simpleexpense.api.ObjectService
import lab.justonebyte.simpleexpense.api.UploadCategoryBatch
import lab.justonebyte.simpleexpense.api.UploadTransactionBatch
import lab.justonebyte.simpleexpense.data.*
import lab.justonebyte.simpleexpense.model.ServerCategory
import lab.justonebyte.simpleexpense.model.ServerTransaction
import lab.justonebyte.simpleexpense.utils.RetrofitHelper

val KEY_NEW_CLIENTS_IDS = "new_client_ids"
val KEY_TABLE_NAME = "table_name"
val TOKEN = "token"
val OBJECTS_STRING = "objects"
class UploadOrUpdateClientObjectsWorker (
    context: Context,
    workerParams: WorkerParameters
)  : CoroutineWorker(context, workerParams) {

    private val scope =  CoroutineScope(SupervisorJob())

    override suspend fun doWork(): Result {
        val tableName = inputData.getString(KEY_VERSION_TABLE)
        val newClientIds = inputData.getStringArray(KEY_NEW_CLIENTS_IDS)
        val token = inputData.getString(TOKEN)?:""


        val categoryDao: CategoryDao =
            AppDatabase.getDatabase(applicationContext,scope).categoryDao()
        val transactionDao: TransactionDao =
            AppDatabase.getDatabase(applicationContext,scope).transactionDao()

        val categoryRepository = CategoryRepositoryImpl(categoryDao)
        val transactionRepository = TransactionRepositoryImpl(transactionDao)
        val allServerTransactions =  transactionRepository.getServerTransactions()
        val allServerCategories =  categoryRepository.getServerCategories()

        val objectService = RetrofitHelper.getInstance(token).create(ObjectService::class.java)


        if (tableName != null) {
            Log.i("update: server:",tableName)
        }else{
            Log.i("update: server:","null")
        }

        if (newClientIds.isNullOrEmpty()) {
           return Result.success()
        }


           Log.i("work manager:","trans size:"+allServerTransactions.size.toString())
           Log.i("work manager:","cats size:"+allServerCategories.size.toString())

        val objectsToUpload = when(tableName){
            "transactions" -> allServerTransactions.filter { transaction -> newClientIds.contains(transaction.unique_id) }
            else -> allServerCategories.filter { category -> newClientIds.contains(category.unique_id) }
        }


        Log.i("work manager:to_upload",objectsToUpload.size.toString())

                when(tableName){
                    "transactions"->objectService.uploadNewOrUpdateTransactions(
                        UploadTransactionBatch(objects = objectsToUpload as List<ServerTransaction>)
                    )
                    else ->objectService.uploadNewOrUpdateCategories(
                        UploadCategoryBatch(objects = objectsToUpload as List<ServerCategory>)
                    )
                }


        return Result.success(inputData)
    }
}

