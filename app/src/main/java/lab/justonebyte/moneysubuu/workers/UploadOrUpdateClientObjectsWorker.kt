package lab.justonebyte.moneysubuu.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.api.ObjectService
import lab.justonebyte.moneysubuu.api.UploadCategoryBatch
import lab.justonebyte.moneysubuu.api.UploadTransactionBatch
import lab.justonebyte.moneysubuu.data.*
import lab.justonebyte.moneysubuu.model.ServerCategory
import lab.justonebyte.moneysubuu.model.ServerTransaction
import lab.justonebyte.moneysubuu.utils.RetrofitHelper

val KEY_NEW_CLIENTS_IDS = "new_client_ids"
val KEY_TABLE_NAME = "table_name"

class UploadOrUpdateClientObjectsWorker (
    context: Context,
    workerParams: WorkerParameters
)  : CoroutineWorker(context, workerParams) {

    private val scope =  CoroutineScope(SupervisorJob())

    override suspend fun doWork(): Result {
        val categoryDao: CategoryDao =
            AppDatabase.getDatabase(applicationContext,scope).categoryDao()
        val transactionDao: TransactionDao =
            AppDatabase.getDatabase(applicationContext,scope).transactionDao()

        val categoryRepository = CategoryRepositoryImpl(categoryDao)
        val transactionRepository = TransactionRepositoryImpl(transactionDao)
        val allServerTransactions =  transactionRepository.getServerTransactions()
        val allServerCategories =  categoryRepository.getServerCategories()

        val objectService = RetrofitHelper.getInstance().create(ObjectService::class.java)


            val newClientIds = inputData.getStringArray(KEY_NEW_CLIENTS_IDS)
            val tableName = inputData.getString(KEY_TABLE_NAME)

        if (newClientIds != null) {
            Log.i("work manager:","ids:"+newClientIds.last())
        }
        Log.i("work manager:","table:"+tableName)

            if(newClientIds==null) return Result.success()

           Log.i("work manager:","trans size:"+allServerTransactions.size.toString())
           Log.i("work manager:","cats size:"+allServerCategories.size.toString())

            val objectsToUpload = when(tableName){
                "transactions"-> allServerTransactions.filter { transaction->newClientIds.contains(transaction.unique_id) }
                else ->  allServerCategories.filter { category->newClientIds.contains(category.unique_id) }
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


        return Result.success()
    }
}
