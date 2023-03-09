package lab.justonebyte.moneysubuu.api

import lab.justonebyte.moneysubuu.model.App
import lab.justonebyte.moneysubuu.model.AppList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class AuthResponse(
    val data:Data
)
data class Data(
    val token:String
)

interface AuthService {
    @GET("get-access-token/{idToken}" )
    suspend fun getAccessToken(@Path("idToken") idToken: String) : Response<AuthResponse>
}
