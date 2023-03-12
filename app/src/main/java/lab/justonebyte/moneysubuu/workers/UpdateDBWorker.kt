package lab.justonebyte.moneysubuu.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class UpdateDBWorker (
    context: Context,
    workerParams: WorkerParameters
)  : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val keys = inputData.getStringArray("keys")
        val values = inputData.getStringArray("values")
        if (keys != null) {
            Log.i("worker first:",keys.first())
        }
        if (values != null) {
            Log.i("worker first:",values.first())
        }

        val pairsList = (keys?.asList() ?: listOf() ) zip (values?.asList() ?: listOf() )

        if (keys != null) {
//            Log.i("worker:",pairsList.size)
        }

        return Result.success()
    }
}

