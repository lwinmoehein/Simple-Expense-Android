package lab.justonebyte.moneysubuu.api

import com.google.gson.annotations.SerializedName
import lab.justonebyte.moneysubuu.data.CategoryEntity
import lab.justonebyte.moneysubuu.data.UniqueIdWithVersion
import lab.justonebyte.moneysubuu.model.ServerCategory
import lab.justonebyte.moneysubuu.model.ServerTransaction
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path



data class CategoryVersionData(
    val new_server_objects:List<ServerCategory>,
    val objects_to_update_client:List<ServerCategory>,
    val objects_to_update_server:List<ServerCategory>,
    val new_client_object_ids:List<String>
)

data class CategoryVersionResponse(
  val data:CategoryVersionData
)

data class TransactionVersionData(
    val new_server_objects:List<ServerTransaction>,
    val objects_to_update_client:List<ServerTransaction>,
    val objects_to_update_server:List<ServerTransaction>,
    val new_client_object_ids:List<String>
)

data class TransactionVersionResponse(
    val data:TransactionVersionData
)
data class ObjectPostData(
    @SerializedName("versions") val versions: List<UniqueIdWithVersion>,
)
data class UploadCategoryBatch(
    @SerializedName("table_name") val table_name: String?="categories",
    @SerializedName("objects") val objects: List<ServerCategory>,
)
data class UploadTransactionBatch(
    @SerializedName("table_name") val table_name: String?="transactions",
    @SerializedName("objects") val objects: List<ServerTransaction>,
)
interface ObjectService {
    @GET("get-access-token/{idToken}" )
    suspend fun getAccessToken(@Path("idToken") idToken: String) : Response<AuthResponse>

    @POST("categories/changes" )
    suspend fun getChangedCategories(@Body objectData: ObjectPostData) : Response<CategoryVersionResponse>

    @POST("transactions/changes" )
    suspend fun getChangedTransactions(@Body objectData: ObjectPostData) : Response<TransactionVersionResponse>

    @POST("batch-objects" )
    suspend fun uploadNewOrUpdateCategories(@Body batchData: UploadCategoryBatch):Response<String>

    @POST("batch-objects" )
    suspend fun uploadNewOrUpdateTransactions(@Body batchData: UploadTransactionBatch):Response<String>
}
