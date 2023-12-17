package lab.justonebyte.simpleexpense.api

import com.google.gson.annotations.SerializedName
import lab.justonebyte.simpleexpense.utils.getCurrentDate
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


data class BetweenPostData(
    @SerializedName("start") val from: String= getCurrentDate(),
    @SerializedName("end") val to:String = getCurrentDate(),
)

interface ExportService {

    @POST("reporting/excel" )
    suspend fun generateExcelFile(@Body postDat: BetweenPostData) : Response<ResponseBody>

    @POST("reporting/pdf" )
    suspend fun generatePDFFile(@Body postData: BetweenPostData) : Response<ResponseBody>

}
