package smarthome.client.data.devices

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import smarthome.client.data.devices.dto.DeviceDetails
import smarthome.client.data.devices.dto.GeneralDeviceAndControllersInfo
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo

interface DevicesApi {
    @GET("api/devices")
    suspend fun getAll(): List<GeneralDeviceAndControllersInfo>
    
    @GET("api/devices/{id}")
    suspend fun getDeviceDetails(@Path("id") id: Long): DeviceDetails
    
    @GET("api/devices/pending")
    suspend fun getPending(): List<GeneralDeviceAndControllersInfo>
    
    @GET("api/devices/active")
    suspend fun getAdded(): List<GeneralDeviceAndControllersInfo>
    
    @POST("api/devices/{id}/accept")
    suspend fun accept(@Path("id") id: Long)
    
    @POST("api/devices/{id}/decline")
    suspend fun decline(@Path("id") id: Long)
}