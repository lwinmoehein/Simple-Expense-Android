package lab.justonebyte.moneysubuu.api

import lab.justonebyte.moneysubuu.model.AppList
import retrofit2.Response
import retrofit2.http.GET

interface CompanionAppService {
    @GET("all_apps_list.json")
    suspend fun getApps() : Response<AppList>
}
