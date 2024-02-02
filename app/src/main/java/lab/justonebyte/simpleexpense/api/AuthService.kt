package lab.justonebyte.simpleexpense.api

import com.google.gson.annotations.SerializedName
import lab.justonebyte.simpleexpense.utils.getCurrentDay
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming

data class AuthResponse(
    val data:Data
)
data class Data(
    val token:String,
    val currency: String
)

data class ProfileResponse(
    val data:ProfileData
)
data class ProfileData(
    val currency: String
)

data class UpdateProfilePostData(
    @SerializedName("currency") val currency: String = "USD",
)


interface AuthService {
    @GET("get-access-token/{idToken}" )
    suspend fun getAccessToken(@Path("idToken") idToken: String) : Response<AuthResponse>

    @POST("users/profile" )
    suspend fun updateProfile(@Body postData: UpdateProfilePostData) : Response<ResponseBody>

    @GET("user" )
    suspend fun getProfile() : Response<ProfileResponse>
}
