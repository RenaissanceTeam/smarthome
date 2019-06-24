package smarthome.raspberry.data.remote.fcm

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface FcmSenderApi {
    @POST("sendFcm/") fun send(@Body body: RequestBody): Call<ResponseBody>
}