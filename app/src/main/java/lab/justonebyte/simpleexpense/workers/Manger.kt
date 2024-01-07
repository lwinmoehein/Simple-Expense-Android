package lab.justonebyte.simpleexpense.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

fun runVersionSync(applicationContext:Context,tableName:String,token:String){
    Log.i("sync:",token)
    try {
        val versionInfoWorker =   OneTimeWorkRequest.Builder(GetVersionInfoWorker::class.java)
            .setInputData(Data.Builder().putString(KEY_TABLE_NAME,tableName).putString(TOKEN,token).build())
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        val updateClientRequest = OneTimeWorkRequest.Builder(UpdateServerObjectsWorker::class.java)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()



        val updateServerRequest = OneTimeWorkRequest.Builder(UploadOrUpdateClientObjectsWorker::class.java)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()


        val chainWorkRequest = WorkManager.getInstance(applicationContext)
            .beginUniqueWork(tableName, ExistingWorkPolicy.REPLACE, versionInfoWorker)
            .then(listOf(updateServerRequest,updateClientRequest))
            .enqueue()
    }catch (e:Exception){
        Log.e("sync:","Background sync failed.")
    }
}