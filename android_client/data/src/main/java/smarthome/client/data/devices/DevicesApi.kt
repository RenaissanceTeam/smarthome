package smarthome.client.data.devices

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Device

interface DevicesApi {
    @GET("api/devices")
    suspend fun getAll(): List<GeneralDeviceInfo>
    
    @GET("api/devices/{id}")
    suspend fun getDeviceDetails(@Path("id") id: Long): Device
    
    @GET("api/devices/pending")
    suspend fun getPending(): List<GeneralDeviceInfo>
    
    @GET("api/devices/active")
    suspend fun getAdded(): List<GeneralDeviceInfo>
    
    @POST("api/devices/{id}/accept")
    suspend fun accept(@Path("id") id: Long)
    
    @POST("api/devices/{id}/decline")
    suspend fun decline(@Path("id") id: Long)
}