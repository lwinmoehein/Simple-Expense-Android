package lab.justonebyte.simpleexpense.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.data.AppDatabase
import lab.justonebyte.simpleexpense.data.CategoryDao
import lab.justonebyte.simpleexpense.data.CategoryRepositoryImpl
import lab.justonebyte.simpleexpense.data.TransactionDao
import lab.justonebyte.simpleexpense.data.TransactionRepositoryImpl
import lab.justonebyte.simpleexpense.data.UniqueIdWithVersion

suspend fun runVersionSync(applicationContext:Context,tableName:String,token:String){
    Log.e("sync:","Background sync:"+tableName)
    val scope = CoroutineScope(Dispatchers.IO)

    val categoryDao: CategoryDao = AppDatabase.getDatabase(applicationContext,scope).categoryDao()
    val transactionDao: TransactionDao = AppDatabase.getDatabase(applicationContext,scope).transactionDao()

    val categoryRepository = CategoryRepositoryImpl(categoryDao)
    val transactionRepository = TransactionRepositoryImpl(transactionDao)

    val transactionUniqueVersions = transactionRepository.getUniqueIdsWithVersions()
    val categoryUniqueVersions = categoryRepository.getUniqueIdsWithVersions()

    val objects: List<List<UniqueIdWithVersion>> = if(tableName=="categories"){
         categoryUniqueVersions.chunked(300)
    }else{
         transactionUniqueVersions.chunked(300)
    }

    if(objects.isEmpty()){
        scope.launch {
            syncBatchVersions(0,applicationContext, listOf(),token,tableName)
        }
    }else{
        objects.forEachIndexed { index, chunkedObjects ->
            scope.launch {
                syncBatchVersions(index,applicationContext,chunkedObjects,token,tableName)
            }
        }
    }
}
fun syncBatchVersions(index:Int,applicationContext:Context,objects:List<UniqueIdWithVersion>,token:String,tableName:String){

    val objectsAsGsonString = Gson().toJson(objects)
    Log.i("sync:json",tableName+":"+objectsAsGsonString)

    Log.i("sync:batch:",tableName)
    try {
        val versionInfoWorker =   OneTimeWorkRequest.Builder(GetVersionInfoWorker::class.java)
            .setInputData(
                Data.Builder().putString(OBJECTS_STRING,objectsAsGsonString).putString(KEY_TABLE_NAME,tableName).putString(TOKEN,token).build()
            )
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        val updateClientRequest = OneTimeWorkRequest.Builder(UpdateServerObjectsWorker::class.java)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()



        val updateServerRequest = OneTimeWorkRequest.Builder(UploadOrUpdateClientObjectsWorker::class.java)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()


        WorkManager.getInstance(applicationContext)
            .beginUniqueWork(tableName+index.toString(), ExistingWorkPolicy.REPLACE, versionInfoWorker)
            .then(listOf(updateServerRequest,updateClientRequest))
            .enqueue()
    }catch (e:Exception){
        Log.e("sync:","Background sync failed.")
    }
}