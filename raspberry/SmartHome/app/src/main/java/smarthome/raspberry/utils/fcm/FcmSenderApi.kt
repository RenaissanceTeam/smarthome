package smarthome.raspberry.utils.fcm

import android.content.Context
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.R


interface FcmSenderApi {
    @POST("sendFcm/") fun send(@Body body: RequestBody): Call<ResponseBody>
}