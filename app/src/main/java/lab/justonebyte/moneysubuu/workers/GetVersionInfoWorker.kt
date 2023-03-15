package lab.justonebyte.moneysubuu.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import lab.justonebyte.moneysubuu.api.ObjectPostData
import lab.justonebyte.moneysubuu.api.ObjectService
import lab.justonebyte.moneysubuu.data.*
import lab.justonebyte.moneysubuu.utils.RetrofitHelper

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

        Log.i("work manager:","versions working")

        val categoryVersions = categoryRepository.getUniqueIdsWithVersions()
        val transactionVersions = transactionRepository.getUniqueIdsWithVersions()


        val objectService = RetrofitHelper.getInstance().create(ObjectService::class.java)

            val result = objectService.getChangedCategories(ObjectPostData(table_name = tableName,versions= if(tableName=="categories") categoryVersions else transactionVersions))

            Log.i("work manager:v_result",result.message())

            result.body()?.let {
                val inputData = Data.Builder().putStringArray(KEY_NEW_CLIENTS_IDS,
                    it.data.new_client_object_ids.toTypedArray()
                ).putString(KEY_TABLE_NAME,tableName).build()
                return Result.success(inputData)
            }


        return Result.success()
    }
}

