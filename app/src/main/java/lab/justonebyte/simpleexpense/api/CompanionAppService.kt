package lab.justonebyte.simpleexpense.api

import lab.justonebyte.simpleexpense.model.AppList
import retrofit2.Response
import retrofit2.http.GET

interface CompanionAppService {
    @GET("all_apps_list.json")
    suspend fun getApps() : Response<AppList>
}
