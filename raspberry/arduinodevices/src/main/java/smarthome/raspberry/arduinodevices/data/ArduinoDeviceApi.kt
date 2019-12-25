package smarthome.raspberry.arduinodevices.data

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import smarthome.library.common.Id
import smarthome.raspberry.arduinodevices.data.dto.ArduinoControllerResponse

interface ArduinoDeviceApi {

    @GET("service")
    suspend fun controllerReadRequest(@Query("id") controllerId: Id): ArduinoControllerResponse

    @POST("service")
    suspend fun controllerWriteRequest(
            @Query("id") controllerId: Id,
            @Query("value") value: String): ArduinoControllerResponse

}


