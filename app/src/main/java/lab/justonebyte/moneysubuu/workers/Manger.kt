package lab.justonebyte.moneysubuu.workers

import android.app.Application
import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

fun runVersionSync(application:Application,applicationContext:Context,tableName:String){
    val workManager = WorkManager.getInstance(application)

    val versionInfoWorker =   OneTimeWorkRequest.Builder(GetVersionInfoWorker::class.java)
        .setInputData(Data.Builder().putString(KEY_TABLE_NAME,tableName).build())
        .build()

    val updateClientRequest = OneTimeWorkRequest.Builder(UpdateServerObjectsWorker::class.java)
        .build()



    val updateServerRequest = OneTimeWorkRequest.Builder(UploadOrUpdateClientObjectsWorker::class.java)
        .build()


    val chainWorkRequest = WorkManager.getInstance(applicationContext)
        .beginUniqueWork(tableName, ExistingWorkPolicy.REPLACE, versionInfoWorker)
        .then(listOf(updateServerRequest,updateClientRequest))
        .enqueue()

}