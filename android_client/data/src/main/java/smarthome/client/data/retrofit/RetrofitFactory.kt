package smarthome.client.data.retrofit

import android.os.SystemClock
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smarthome.client.domain.api.auth.usecases.GetCurrentTokenUseCase
import smarthome.client.util.Data

class RetrofitFactory(
    private val urlHolder: HomeServerUrlHolder,
    private val getCurrentTokenUseCase: GetCurrentTokenUseCase
) {
    private var url: String? = null
    private var retrofit: Retrofit? = null
    
    fun getInstance(): Retrofit {
        val currentUrl = urlHolder.get()
        
        if (url == currentUrl) { retrofit?.let { return it } }
        
        url = currentUrl
        
        val httpClient = addAuthorizationHeaderInHttpClient()
    
        return Retrofit.Builder()
            .baseUrl(urlHolder.get())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .also { retrofit = it }
    }
    
    fun <T> createApi(c: Class<T>): T {
        return getInstance().create(c)
    }
    
    private fun addAuthorizationHeaderInHttpClient() = OkHttpClient.Builder()
        .addInterceptor { chain ->
            SystemClock.sleep(1000)
            val ongoing = chain.request().newBuilder()
            val currentToken = getCurrentTokenUseCase.execute()
            if (currentToken is Data) {
                ongoing.addHeader("Authorization", "Bearer ${currentToken.data}")
            }
            chain.proceed(ongoing.build())
        }
        .build()
}