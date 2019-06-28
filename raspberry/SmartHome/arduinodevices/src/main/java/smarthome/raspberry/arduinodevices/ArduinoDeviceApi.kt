package smarthome.raspberry.arduinodevices

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ArduinoDeviceApi {

    @GET("service")
    suspend fun controllerReadRequest(@Query("index") serviceIndex: Int): ArduinoControllerResponse

    @POST("service")
    suspend fun controllerWriteRequest(
            @Query("index") serviceIndex: Int,
            @Query("value") value: String): ArduinoControllerResponse

}


class ArduinoControllerResponse(val response: String)

