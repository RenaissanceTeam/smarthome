package smarthome.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import smarthome.library.common.RaspberryResponse
import smarthome.library.common.SmartHome

interface RaspberryApi {

    @GET("info")
    fun getSmartHomeState(): Call<SmartHome>

    // TODO: 12/20/18 maybe read/write should return different responses? or create many read responses
    // e.g. for temperature, ON/OFF, light, etc..
    @GET("controller")
    fun readControllerState(@Query("controller_guid") controllerGuid: Long): Call<RaspberryResponse>

    @POST("controller")
    fun changeControllerState(@Query("controller_guid") controllerGuid: Long,
                              @Query("value") value: String): Call<RaspberryResponse>
}
