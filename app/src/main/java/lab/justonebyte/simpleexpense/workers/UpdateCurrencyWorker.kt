package lab.justonebyte.simpleexpense.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import lab.justonebyte.simpleexpense.api.AuthService
import lab.justonebyte.simpleexpense.api.UpdateProfilePostData
import lab.justonebyte.simpleexpense.utils.RetrofitHelper

val CURRENCY_CODE="currency_code"

class UpdateCurrencyWorker (
    context: Context,
    workerParams: WorkerParameters
)  : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val token = inputData.getString(TOKEN)?:""
        val currencyCode = inputData.getString(CURRENCY_CODE)?:""

        return try{
            val profileService =
                RetrofitHelper.getInstance(token).create(AuthService::class.java)
            val response = profileService.updateProfile(UpdateProfilePostData(currencyCode))

            if(response.code()==200){
                Result.success()
            }else{
                Result.retry()
            }
        }catch (e:Exception){
            Result.retry()
        }
    }
}

