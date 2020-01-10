package smarthome.client.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory(private val urlHolder: HomeServerUrlHolder) {
    private var url: String? = null
    private var retrofit: Retrofit? = null
    
    fun getInstance(): Retrofit {
        val currentUrl = urlHolder.get()
        
        if (url == currentUrl) {
            retrofit?.let { return it }
        }
        url = currentUrl
        return Retrofit.Builder()
            .baseUrl(urlHolder.get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .also { retrofit = it }
    }
}