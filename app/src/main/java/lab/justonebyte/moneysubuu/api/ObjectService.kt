package lab.justonebyte.moneysubuu.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path



interface ObjectService {
    @GET("get-access-token/{idToken}" )
    suspend fun getAccessToken(@Path("idToken") idToken: String) : Response<AuthResponse>
}
