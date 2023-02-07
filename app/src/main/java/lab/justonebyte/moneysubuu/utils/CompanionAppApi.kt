package lab.justonebyte.moneysubuu.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AppsApi {
    @GET("all_apps_list.json")
    suspend fun getApps() : Response<AppList>
}
