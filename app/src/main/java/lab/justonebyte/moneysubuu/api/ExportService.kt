package lab.justonebyte.moneysubuu.api

import com.google.gson.annotations.SerializedName
import lab.justonebyte.moneysubuu.data.CategoryEntity
import lab.justonebyte.moneysubuu.data.UniqueIdWithVersion
import lab.justonebyte.moneysubuu.model.ServerCategory
import lab.justonebyte.moneysubuu.model.ServerTransaction
import lab.justonebyte.moneysubuu.utils.getCurrentDate
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path





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
