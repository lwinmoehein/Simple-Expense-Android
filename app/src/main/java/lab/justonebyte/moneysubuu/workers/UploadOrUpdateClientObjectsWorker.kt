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
    private var allCategories = listOf<ServerCategory>()
    private var allTransactions = listOf<ServerTransaction>()


    override suspend fun doWork(): Result {
        val categoryDao: CategoryDao =
            AppDatabase.getDatabase(applicationContext,scope).categoryDao()
        val transactionDao: TransactionDao =
            AppDatabase.getDatabase(applicationContext,scope).transactionDao()

        val categoryRepository = CategoryRepositoryImpl(categoryDao)
        val transactionRepository = TransactionRepositoryImpl(transactionDao)

        val objectService = RetrofitHelper.getInstance().create(ObjectService::class.java)


        Log.i("work manager:","update or new working")

            scope.launch {
                categoryRepository.getServerCategories().collect{
                    allCategories = it
                    Log.i("work manager:all_cats",allCategories.size.toString())
                }
            }
            scope.launch {
                transactionRepository.getServerTransactions().collect{
                    allTransactions = it
                    Log.i("work manager:all_trans",allTransactions.size.toString())
                }
            }

            val newClientIds = inputData.getStringArray(KEY_NEW_CLIENTS_IDS)
            val tableName = inputData.getString(KEY_TABLE_NAME)

        if (newClientIds != null) {
            Log.i("work manager:","ids:"+newClientIds.size.toString())
        }
        Log.i("work manager:","table:"+tableName)

            if(newClientIds==null) return Result.success()

            val transactionsToUpload = when(tableName){
                "transactions"->allTransactions.filter { transaction->newClientIds.contains(transaction.unique_id) }
                else -> allCategories.filter { category->newClientIds.contains(category.unique_id) }
            }

            Log.i("work manager:to_upload",transactionsToUpload.size.toString())

            scope.launch {
                when(tableName){
                    "transactions"->objectService.uploadNewOrUpdateTransactions(
                        UploadTransactionBatch(objects = transactionsToUpload as List<ServerTransaction>)
                    )
                    else ->objectService.uploadNewOrUpdateCategories(
                        UploadCategoryBatch(objects = transactionsToUpload as List<ServerCategory>)
                    )
                }
            }

        return Result.success()
    }
}

