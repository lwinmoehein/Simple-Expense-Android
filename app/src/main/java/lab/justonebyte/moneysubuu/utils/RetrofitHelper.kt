package lab.justonebyte.moneysubuu.utils

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer 1|hkNhJOjXPd8rO36rvTcaHcesaJOkyLlY29xhUU9d")
            .build()
        return chain.proceed(request)
    }
}

object RetrofitHelper {

    val baseUrl = "http://192.168.1.8:9000/api/"


    fun getInstance(): Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor("your_token_here"))
            .build()

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}