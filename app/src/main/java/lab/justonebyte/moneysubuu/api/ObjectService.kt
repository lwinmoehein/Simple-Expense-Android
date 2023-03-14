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


data class CategoryServerDTO(
    val uniqueId:String,
    val name:String,
    val photo_url:String?,
    val deleted_at:String?,
    val created_at:String,
    val updated_at:String,
    val version:Int
    )
data class ObjectVersionData(
    val new_server_objects:List<CategoryServerDTO>,
    val objects_to_update_client:List<CategoryServerDTO>,
    val objects_to_update_server:List<CategoryServerDTO>,
    val new_client_object_ids:List<String>
)

data class ChangeObjectResponse(
  val data:ObjectVersionData
)
data class ObjectPostData(
    @SerializedName("table_name") val table_name: String?="categories",
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

    @POST("changed-objects" )
    suspend fun getChangedCategories(@Body objectData: ObjectPostData) : Response<ChangeObjectResponse>

    @POST("batch-objects" )
    suspend fun uploadNewOrUpdateCategories(@Body batchData: UploadCategoryBatch):Response<String>

    @POST("batch-objects" )
    suspend fun uploadNewOrUpdateTransactions(@Body batchData: UploadTransactionBatch):Response<String>
}
